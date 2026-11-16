package com.geobus.paradas;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "paradas")
public class Parada {

    @Id
    @Column(name = "parada_id", nullable = false)
    private String id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "latitud", nullable = false)
    private BigDecimal latitud;

    @Column(name = "longitud", nullable = false)
    private BigDecimal longitud;

    @Column(name = "codigo")
    private String codigo;

    protected Parada() {
    }

    public Parada(String id, String nombre, BigDecimal latitud, BigDecimal longitud, String codigo) {
        this.id = id;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.codigo = codigo;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public String getCodigo() {
        return codigo;
    }
}
