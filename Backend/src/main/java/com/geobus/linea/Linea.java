package com.geobus.linea;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lineas")
public class Linea {

    @Id
    @Column(name = "linea_id", nullable = false)
    private String id;

    @Column(name = "nombre_corto", nullable = false)
    private String nombreCorto;

    @Column(name = "nombre_largo")
    private String nombreLargo;

    protected Linea() {
    }

    public Linea(String id, String nombreCorto, String nombreLargo) {
        this.id = id;
        this.nombreCorto = nombreCorto;
        this.nombreLargo = nombreLargo;
    }

    public String getId() {
        return id;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public String getNombreLargo() {
        return nombreLargo;
    }
}
