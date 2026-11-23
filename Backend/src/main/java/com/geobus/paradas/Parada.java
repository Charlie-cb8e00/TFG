
package com.geobus.paradas;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "parada")
public class Parada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_ctmas", unique = true)
    private Integer codigoCtmas;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "latitud")
    private BigDecimal latitud;

    @Column(name = "longitud")
    private BigDecimal longitud;

    public Parada() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getCodigoCtmas() {
        return codigoCtmas;
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

    public void setCodigoCtmas(Integer codigoCtmas) {
        this.codigoCtmas = codigoCtmas;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }
}
