package com.geobus.gtfs;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Importa un feed GTFS (zip) a las tablas de GeoBus.
 * Usa INSERT ... ON CONFLICT DO UPDATE: nunca borra, así que no se pierden
 * favoritos ni avisos de los usuarios.
 *
 * Requiere @EnableScheduling en la clase principal si quieres la carga programada.
 */
@Service
public class GtfsImportService {

    private static final Logger log = LoggerFactory.getLogger(GtfsImportService.class);
    private static final int LOTE = 5000;

    private final JdbcTemplate jdbc;

    @Value("${gtfs.url}")
    private String gtfsUrl; // https://... o file:///ruta/gtfs.zip

    public GtfsImportService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Se ejecuta cada noche a las 4:00. Quítalo si prefieres lanzarlo a mano. */
    @Scheduled(cron = "0 0 4 * * *")
    public void importarProgramado() throws IOException {
        importar();
    }

    // Todo en una transacción: si algo falla, no queda la BD a medias.
    @Transactional(rollbackFor = Exception.class)
    public void importar() throws IOException {
        Path tmp = Files.createTempFile("gtfs", ".zip");
        try {
            try (InputStream in = URI.create(gtfsUrl).toURL().openStream()) {
                Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
            }
            try (ZipFile zip = new ZipFile(tmp.toFile())) {
                // Orden obligatorio por las claves foráneas
                cargarParadas(zip);
                cargarLineas(zip);
                cargarCalendario(zip);
                cargarViajes(zip);
                cargarHorasDePaso(zip);
            }
            log.info("Importación GTFS terminada");
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    // ---------- una función por tabla ----------

    private void cargarParadas(ZipFile zip) throws IOException {
        cargar(zip, "stops.txt", """
                INSERT INTO paradas (parada_id, nombre, latitud, longitud, codigo)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT (parada_id) DO UPDATE SET
                    nombre = EXCLUDED.nombre, latitud = EXCLUDED.latitud,
                    longitud = EXCLUDED.longitud, codigo = EXCLUDED.codigo
                """, r -> new Object[]{
                r.get("stop_id"), r.get("stop_name"),
                new BigDecimal(r.get("stop_lat")), new BigDecimal(r.get("stop_lon")),
                opt(r, "stop_code")});
    }

    private void cargarLineas(ZipFile zip) throws IOException {
        cargar(zip, "routes.txt", """
                INSERT INTO lineas (linea_id, nombre_corto, nombre_largo)
                VALUES (?, ?, ?)
                ON CONFLICT (linea_id) DO UPDATE SET
                    nombre_corto = EXCLUDED.nombre_corto, nombre_largo = EXCLUDED.nombre_largo
                """, r -> {
            String corto = opt(r, "route_short_name");
            return new Object[]{
                    r.get("route_id"),
                    corto != null ? corto : r.get("route_id"), // nombre_corto es obligatorio
                    opt(r, "route_long_name")};
        });
    }

    private void cargarCalendario(ZipFile zip) throws IOException {
        cargar(zip, "calendar.txt", """
                INSERT INTO calendario (servicio_id, lunes, martes, miercoles, jueves, viernes,
                                        sabado, domingo, fecha_inicio, fecha_fin)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT (servicio_id) DO UPDATE SET
                    lunes = EXCLUDED.lunes, martes = EXCLUDED.martes, miercoles = EXCLUDED.miercoles,
                    jueves = EXCLUDED.jueves, viernes = EXCLUDED.viernes, sabado = EXCLUDED.sabado,
                    domingo = EXCLUDED.domingo, fecha_inicio = EXCLUDED.fecha_inicio,
                    fecha_fin = EXCLUDED.fecha_fin
                """, r -> new Object[]{
                r.get("service_id"),
                Integer.parseInt(r.get("monday")), Integer.parseInt(r.get("tuesday")),
                Integer.parseInt(r.get("wednesday")), Integer.parseInt(r.get("thursday")),
                Integer.parseInt(r.get("friday")), Integer.parseInt(r.get("saturday")),
                Integer.parseInt(r.get("sunday")),
                fecha(r.get("start_date")), fecha(r.get("end_date"))});
    }

    private void cargarViajes(ZipFile zip) throws IOException {
        cargar(zip, "trips.txt", """
                INSERT INTO viajes (viaje_id, linea_id, servicio_id, destino)
                VALUES (?, ?, ?, ?)
                ON CONFLICT (viaje_id) DO UPDATE SET
                    linea_id = EXCLUDED.linea_id, servicio_id = EXCLUDED.servicio_id,
                    destino = EXCLUDED.destino
                """, r -> new Object[]{
                r.get("trip_id"), r.get("route_id"), r.get("service_id"),
                opt(r, "trip_headsign")});
    }

    private void cargarHorasDePaso(ZipFile zip) throws IOException {
        // '25:10:00' se convierte bien a INTERVAL en PostgreSQL
        cargar(zip, "stop_times.txt", """
                INSERT INTO horas_de_paso (viaje_id, orden_parada, parada_id, hora_llegada, hora_salida)
                VALUES (?, ?, ?, CAST(? AS interval), CAST(? AS interval))
                ON CONFLICT (viaje_id, orden_parada) DO UPDATE SET
                    parada_id = EXCLUDED.parada_id, hora_llegada = EXCLUDED.hora_llegada,
                    hora_salida = EXCLUDED.hora_salida
                """, r -> {
            String llegada = opt(r, "arrival_time");
            String salida = opt(r, "departure_time");
            if (llegada == null) llegada = salida;
            if (salida == null) salida = llegada;
            if (llegada == null) return null; // sin hora no se puede guardar (columnas obligatorias)
            return new Object[]{
                    r.get("trip_id"), Integer.parseInt(r.get("stop_sequence")),
                    r.get("stop_id"), llegada, salida};
        });
    }

    // ---------- utilidades ----------

    /** Lee un CSV del zip y lo inserta por lotes. Si 'fila' devuelve null, se salta la fila. */
    private void cargar(ZipFile zip, String fichero, String sql,
                        Function<CSVRecord, Object[]> fila) throws IOException {
        ZipEntry entrada = zip.getEntry(fichero);
        if (entrada == null) {
            throw new IllegalStateException("El GTFS no contiene " + fichero);
        }
        List<Object[]> lote = new ArrayList<>(LOTE);
        int total = 0;

        try (Reader reader = sinBom(new InputStreamReader(zip.getInputStream(entrada), StandardCharsets.UTF_8));
             CSVParser parser = CSVFormat.DEFAULT.builder()
                     .setHeader().setSkipHeaderRecord(true).setTrim(true).build().parse(reader)) {
            for (CSVRecord registro : parser) {
                Object[] args = fila.apply(registro);
                if (args == null) continue;
                lote.add(args);
                if (lote.size() == LOTE) {
                    jdbc.batchUpdate(sql, lote);
                    total += lote.size();
                    lote.clear();
                }
            }
        }
        if (!lote.isEmpty()) {
            jdbc.batchUpdate(sql, lote);
            total += lote.size();
        }
        log.info("{}: {} filas procesadas", fichero, total);
    }

    /** Valor opcional: null si la columna no existe o está vacía. */
    private static String opt(CSVRecord r, String columna) {
        if (!r.isMapped(columna) || !r.isSet(columna)) return null;
        String v = r.get(columna);
        return (v == null || v.isBlank()) ? null : v;
    }

    private static Date fecha(String yyyymmdd) {
        return Date.valueOf(LocalDate.parse(yyyymmdd, DateTimeFormatter.BASIC_ISO_DATE));
    }

    /** Muchos GTFS empiezan con BOM (\uFEFF), que rompería el nombre de la primera columna. */
    private static Reader sinBom(Reader reader) throws IOException {
        PushbackReader pr = new PushbackReader(reader, 1);
        int c = pr.read();
        if (c != -1 && c != '\uFEFF') pr.unread(c);
        return pr;
    }
}
