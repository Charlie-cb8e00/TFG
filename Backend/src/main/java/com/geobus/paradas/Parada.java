
package com.geobus.paradas;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "parada")
public class Parada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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

    public Parada(String nombre, BigDecimal latitud, BigDecimal longitud, Long lineaId) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.lineaId = lineaId;
    }

    public Long getId() {
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
