# GeoBus

GeoBus es un prototipo de aplicación para consultar paradas de autobús y mostrar
un recorrido entre Dos Hermanas y Sevilla. El repositorio contiene una interfaz
web estática y un backend Java con Spring Boot. A día de hoy son dos partes
separadas: las páginas web no llaman al backend.

Este documento describe los archivos propios del proyecto, el comportamiento de
cada sección y, con más detalle, el JavaScript. No intenta documentar el código
interno de Bootstrap, Leaflet, Spring Boot ni otras dependencias de terceros.

## Estructura del proyecto

```text
Frontend/
  HTML/
    index.html
    map.html
    stops.html
    wallet.html
    alarms.html
  CSS/
    styles.css
  JavaScript/
    script.js
    bus-stops.js
Backend/
  src/main/java/com/geobus/
  src/main/resources/application.properties
  src/test/java/
  pom.xml
  mvnw
  mvnw.cmd
package.json
package-lock.json
```

- `Frontend/HTML/` contiene las cinco páginas de la aplicación.
- `Frontend/CSS/styles.css` define el estilo propio compartido por las páginas.
- `Frontend/JavaScript/script.js` gestiona las alarmas y las tarjetas del
  monedero.
- `Frontend/JavaScript/bus-stops.js` contiene los datos de la línea 242 y crea
  la lista de paradas cuando se abre esa sección.
- `Backend/src/main/java/` contiene la aplicación Spring Boot, el endpoint de
  paradas, las entidades JPA y el importador GTFS.
- `Backend/src/main/resources/application.properties` contiene la configuración
  de la aplicación y de PostgreSQL.
- `Backend/src/test/java/` no contiene actualmente pruebas implementadas.
- `package.json` no define scripts de construcción ni de ejecución. Las páginas
  cargan Bootstrap desde un CDN; Leaflet se usa en el mapa.

## Interfaz web

### Estructura compartida

Las páginas usan HTML semántico (`header`, `main` y `nav`) y utilidades de
Bootstrap para la disposición. La clase `.app` limita el ancho de la interfaz y
la presenta como una aplicación móvil centrada. Cada página tiene:

1. Una cabecera con la marca y el icono de la sección.
2. Un título con el estilo `.titlebar`.
3. Un contenido principal que puede desplazarse.
4. Una barra inferior con enlaces a Mapa, Paradas, Inicio, Monedero y Alarma.

Los enlaces de navegación son enlaces HTML normales. No necesitan JavaScript
para cambiar de página.

### Inicio: `Frontend/HTML/index.html`

La página de inicio presenta una parada favorita de ejemplo: Avenida de
Andalucía, con las líneas 32 y 39. El enlace de esa tarjeta abre `stops.html`.
El contenido es estático y no se obtiene del backend ni se guarda como
preferencia de usuario.

### Mapa: `Frontend/HTML/map.html`

El mapa se crea con Leaflet y muestra las teselas de OpenStreetMap:

1. Se crea el mapa asociado al elemento `#map`.
2. Se añade una capa de teselas con la atribución requerida a OpenStreetMap.
3. Se cargan los registros de `bus-stops.js`.
4. Se ordenan por sentido y número de parada para formar dos recorridos:
   - Sentido 1: Dos Hermanas → Sevilla, línea azul.
   - Sentido 2: Sevilla → Dos Hermanas, línea naranja discontinua.
5. Se agrupan los registros con el mismo identificador de parada para no dibujar
   dos marcadores en el mismo punto cuando una parada pertenece a ambos
   sentidos. Por eso se representan 40 ubicaciones físicas a partir de 42
   registros de recorrido.
6. Cada ubicación es un `circleMarker`. Sus colores identifican el sentido y
   las paradas compartidas. Al seleccionar un marcador se ven el nombre, el
   sentido y el orden de parada.
7. El encuadre se ajusta para incluir los marcadores y ambas líneas.

La línea y los marcadores usan las coordenadas del conjunto local de datos; no
se consulta una API de rutas. La capa de fondo de OpenStreetMap sí requiere
conexión a Internet. El enlace «Abrir mapa completo» abre OpenStreetMap en otra
página.

### Paradas: `Frontend/HTML/stops.html`

`#listaParadas` es el contenedor vacío de la lista. `bus-stops.js` lo rellena
con las 42 entradas de la línea 242, agrupadas y ordenadas por sentido. Cada
fila muestra el número de línea, nombre, identificador y número de orden. Los
encabezados indican el sentido de ida o vuelta.

### Monedero: `Frontend/HTML/wallet.html`

La página empieza con tres tarjetas de ejemplo. El botón flotante rojo `+`
abre un modal con los campos de nombre y descripción. Al confirmar, JavaScript
crea una tarjeta y la añade a la lista. Los datos se mantienen solamente en el
DOM de esa página: al recargarla se vuelven a mostrar las tarjetas iniciales.

### Alarmas: `Frontend/HTML/alarms.html`

La página empieza con una alarma de ejemplo. El botón flotante `+` abre el
formulario para añadir otra. El mismo modal se reutiliza para editar una alarma.
Los campos de hora, nombre y parada son obligatorios en el formulario. La
eliminación solicita confirmación en un segundo modal.

Las alarmas creadas, editadas o eliminadas solo cambian la página actual; no se
guardan en almacenamiento local ni en el backend.

## JavaScript

### `Frontend/JavaScript/script.js`

Este archivo contiene las interacciones de alarma y monedero. Se carga en esas
dos páginas después de Bootstrap.

#### Inicialización

`DOMContentLoaded` espera a que el documento esté construido y llama a
`activarAlarmas()` y `activarMonedero()`. Cada función comprueba si existen los
elementos que necesita. Así, cargar el archivo en una página que no tiene ese
formulario no provoca un error.

La función `irA(event, url)` cambia `window.location.href` a una dirección
indicada. Actualmente las páginas usan enlaces HTML directamente, por lo que
esta función auxiliar no está conectada a los controles de navegación.

#### `activarAlarmas()`

1. Busca el contenido de alarmas, el formulario y el modal de edición. Si falta
   alguno, termina sin modificar la página.
2. Obtiene las instancias de los modales de edición y eliminación mediante
   `bootstrap.Modal.getOrCreateInstance`.
3. Al pulsar `#btnNuevaAlarma`, limpia el formulario, deja `alarmaActual` a
   `null`, cambia el título y el texto del botón, y abre el modal. El valor
   `null` indica que el envío creará una alarma nueva.
4. Los botones de editar y eliminar se atienden mediante un único listener en
   el contenedor de alarmas. Esta delegación permite que las alarmas añadidas
   dinámicamente tengan las mismas acciones.
5. Editar copia los valores de la tarjeta al formulario, actualiza el título y
   abre el modal.
6. Eliminar guarda la tarjeta seleccionada en `alarmaActual` y muestra el modal
   de confirmación.
7. Al enviar el formulario, se evita la recarga del navegador y se leen los
   valores con `trim()`:
   - Si `alarmaActual` contiene una tarjeta, se actualizan sus tres campos.
   - Si es `null`, se crean los elementos HTML de una alarma nueva y se añaden
     al contenedor. Los valores del usuario se asignan con `textContent`.
8. Tras guardar, se cierra el modal.
9. Al confirmar la eliminación, se elimina del DOM la tarjeta seleccionada y se
   cierra el modal.

La referencia `alarmaActual` solo vive mientras la página está abierta; no es
un identificador persistente ni se sincroniza con el servidor.

#### `activarMonedero()`

1. Busca el formulario y la lista de tarjetas. Si no existen, termina.
2. Obtiene la instancia Bootstrap del modal de alta.
3. Al pulsar `#btnNuevaTarjeta`, limpia los campos y abre el modal.
4. Al enviar el formulario, evita la recarga, crea los elementos de una tarjeta
   (icono, nombre y descripción) y los añade a `.list-group`.
5. Los valores introducidos se escriben con `textContent`, no como HTML.
6. Cierra el modal después de añadir la tarjeta.

La tarjeta nueva tampoco se conserva al recargar.

### `Frontend/JavaScript/bus-stops.js`

#### Datos

`paradasLinea242` es el conjunto compartido por la lista y el mapa. Cada objeto
contiene:

- `id`: identificador de la parada.
- `nombre`: nombre tal como se recibió en los datos.
- `lat` y `lng`: latitud y longitud decimal.
- `sentido`: 1 para la ida y 2 para la vuelta.
- `orden`: posición de la parada dentro de ese sentido.

El archivo contiene 42 registros: 17 de ida y 25 de vuelta. Los identificadores
2456 y 3021 aparecen en ambos sentidos porque son paradas compartidas.

#### Construcción de la lista

Al recibir `DOMContentLoaded`, el archivo busca `#listaParadas`. Si no existe
(por ejemplo, en el mapa), no construye una lista. Si existe:

1. Crea un `DocumentFragment` para agrupar los elementos antes de insertarlos
   en la página.
2. Recorre los sentidos 1 y 2, creando el encabezado correspondiente.
3. Filtra los registros por sentido y los ordena por `orden`.
4. Construye cada fila con elementos DOM: número de línea, nombre, ID y orden.
5. Inserta el fragmento completo en `#listaParadas`.

Los nombres se colocan mediante `textContent`, de modo que se presentan como
texto y no se interpretan como marcado HTML.

## Estilos: `Frontend/CSS/styles.css`

El CSS propio complementa las utilidades de Bootstrap:

- `:root` define los colores base de GeoBus.
- `body` configura el fondo y el color general.
- `.app` centra la aplicación, limita su ancho, establece el fondo y recorta el
  contenido al marco de la interfaz.
- `.topbar` y `.titlebar` dan formato a la cabecera y al título.
- `.content` permite que el área principal se reduzca dentro de la columna y se
  desplace verticalmente.
- `.tabbar`, `.tab` y sus estados `:hover`/`.tab--active` estilizan la
  navegación inferior.
- `.stop`, `.stop__line`, `.stop__info`, `.stop__name`, `.stop__tags` y `.tag`
  componen cada parada y sus etiquetas.
- `.btn-add-floating` presenta el botón `+` como control circular rojo situado
  sobre la barra inferior; sus estados `:hover`, `:active` y `:focus-visible`
  proporcionan respuesta visual y foco accesible.
- `.alarm`, `.alarm__time`, `.alarm__info` y `.alarm__actions` organizan cada
  alarma; `.btn-edit` y `.btn-delete` distinguen sus acciones.
- `.wallet-card` y `.wallet-card__icon` dan formato a las tarjetas del
  monedero.
- `.map-frame` reserva el espacio flexible del mapa y establece una altura
  mínima.
- La media query de 576 px ajusta la altura y altura mínima de `.app` en
  pantallas más anchas.

## Backend

### Inicio de Spring Boot

`Backend/src/main/java/com/geobus/GeoBusApplication.java` es el punto de entrada.
`@SpringBootApplication` activa la configuración y el escaneo de componentes de
Spring; `@EnableScheduling` permite ejecutar tareas programadas, como la
importación GTFS.

### API de paradas

- `paradas/ParadaController.java` expone `GET /api/paradas`. Inyecta el
  repositorio, llama a `findAll()` y Spring serializa la lista como JSON.
  `@CrossOrigin(origins = "*")` permite peticiones desde cualquier origen.
- `paradas/ParadaRepository.java` extiende `JpaRepository` y proporciona
  operaciones de persistencia estándar sin implementación propia.
- `paradas/Parada.java` representa una parada con nombre, latitud, longitud y
  referencia a una línea. Incluye restricciones de validación.

La API solo es utilizable si la aplicación compila, PostgreSQL está accesible y
la tabla esperada existe.

### Entidades JPA

Las clases anotadas con `@Entity` representan tablas. La mayoría usa un
constructor sin argumentos protegido para JPA, constructores públicos para
crear instancias y getters para leer los campos. Los IDs y las referencias a
otras entidades suelen guardarse como `Long`; no se definen asociaciones JPA
`@ManyToOne` entre esas tablas.

- `autobuses/Autobus.java`: ID generado y nombre obligatorio del autobús.
- `avisos/Aviso.java`: nombre y tiempo de aviso, además de los IDs de usuario y
  parada. El tiempo está limitado a 1–120 y hay una restricción única para la
  combinación usuario, parada y tiempo.
- `lineas/Linea.java`: ID generado y nombre obligatorio de la línea.
- `lineas_autobuses/LineaAutobus.java`: relación entre una línea y un autobús,
  identificada con una clave compuesta.
- `lineas_autobuses/LineaAutobusId.java`: clave compuesta serializable. Define
  `equals` y `hashCode` a partir de los dos IDs, requeridos para identificar
  correctamente claves embebidas.
- `monederos/Monedero.java`: nombre, tipo de tarjeta, saldo `BigDecimal` no
  negativo y referencia al usuario.
- `usuarios/Usuario.java`: nombre, correo único validado como email y campo
  `contrasenaHash`. La clase solo modela el dato; no implementa autenticación ni
  generación del hash.
- `usuarios_paradas/UsuarioParada.java`: relación usuario-parada con un
  indicador booleano de favorito.
- `usuarios_paradas/UsuarioParadaId.java`: clave compuesta usuario-parada, con
  `equals` y `hashCode`.

Las anotaciones `@NotBlank`, `@NotNull`, `@Email`, `@Min`, `@Max`,
`@DecimalMin` y `@DecimalMax` expresan validaciones de los campos, siempre que
se invoque la validación correspondiente.

### Importación GTFS: `gtfs/GtfsImportService.java`

Este servicio opcional importa un ZIP GTFS a PostgreSQL:

1. Solo se crea si `gtfs.import.enabled=true`.
2. Obtiene el ZIP desde `gtfs.url`, lo copia a un temporal y lo abre.
3. Procesa los archivos en este orden: `stops.txt`, `routes.txt`,
   `calendar.txt`, `trips.txt` y `stop_times.txt`.
4. Lee CSV UTF-8 con Apache Commons CSV, quita un posible BOM y acepta columnas
   opcionales mediante `opt`.
5. Convierte fechas GTFS a fechas SQL, coordenadas a `BigDecimal` e inserta
   filas por lotes de 5.000.
6. Usa `INSERT ... ON CONFLICT DO UPDATE` para actualizar los datos existentes
   en vez de borrarlos.
7. Si falta un archivo requerido o ocurre un error, falla la importación; la
   operación tiene transacción y el ZIP temporal se elimina en `finally`.
8. `@Scheduled(cron = "0 0 4 * * *")` programa una importación diaria a las
   04:00 cuando el servicio está habilitado.

El importador está desactivado en la configuración actual. Sus nombres de tabla
no coinciden con todos los nombres de las entidades del entregable; véanse las
limitaciones actuales más abajo.

### Configuración del backend

`Backend/src/main/resources/application.properties`:

- Define el nombre `geobus`.
- Configura la conexión PostgreSQL y el usuario.
- Obtiene la contraseña desde la variable de entorno `DB_PASSWORD`; no debería
  escribirse en el README ni en el control de versiones.
- Establece `spring.jpa.hibernate.ddl-auto=none`: Hibernate no crea ni modifica
  tablas.
- Configura la URL del feed GTFS y deja `gtfs.import.enabled=false`.

`Backend/pom.xml` define Java 25, Spring Boot, las dependencias web/JPA,
validación, PostgreSQL, Commons CSV y las dependencias de prueba. `mvnw` y
`mvnw.cmd` son los wrappers de Maven para macOS/Linux y Windows. La configuración
de depuración de VS Code está en `Backend/.vscode/launch.json`.

## Cómo abrir y ejecutar

### Frontend

Abre `Frontend/HTML/index.html` en un navegador o sirve la carpeta `Frontend`
con un servidor estático. No hay paso de compilación. Para cargar Bootstrap,
Leaflet y las teselas de OpenStreetMap se necesita conexión a Internet.

### Backend

Se requiere Java 25, PostgreSQL accesible desde la URL configurada y una base de
datos con el esquema esperado por las entidades:

```powershell
cd Backend
$env:DB_PASSWORD = "<contraseña local>"
.\mvnw.cmd spring-boot:run
```

La configuración actual espera que la base de datos ya exista. El repositorio
no incluye un `schema.sql` ni una migración que la cree.

## Límites y estado actual

- Las páginas del frontend son una maqueta independiente: no hacen peticiones a
  `GET /api/paradas`.
- Favoritos, alarmas y tarjetas de ejemplo están escritos en HTML. Las altas,
  ediciones y eliminaciones de alarmas o tarjetas se guardan solo en el DOM y
  desaparecen al recargar la página.
- `index.html` sigue mostrando una parada favorita ficticia de la línea 388; la
  lista y el mapa usan los datos estáticos de la línea 242.
- La ruta del mapa une las coordenadas de las paradas por orden. No consulta un
  servicio de direcciones ni calcula calles reales.
- `spring.jpa.hibernate.ddl-auto=none` requiere un esquema de PostgreSQL creado
  por separado; no hay `schema.sql` en los recursos actuales.
- `Parada.java` tiene un getter `getCodigo()` que devuelve `codigo`, pero la
  entidad no declara ese campo. Hay que resolver esa inconsistencia antes de
  poder compilar el backend.
- `ParadaRepository` declara `Integer` como tipo de ID mientras `Parada` declara
  el ID como `Long`; ambos tipos deberían coincidir para un uso correcto de JPA.
- El importador GTFS está deshabilitado. Además, escribe en tablas como
  `paradas` y `lineas`, mientras algunas entidades están mapeadas a `parada` y
  `linea`; hay que armonizar el esquema antes de habilitarlo.
- Las páginas cargan Bootstrap 5.3.3 desde CDN; `package.json` declara una
  dependencia Bootstrap distinta y no ofrece scripts frontend. La versión
  usada por la interfaz es la del CDN.

