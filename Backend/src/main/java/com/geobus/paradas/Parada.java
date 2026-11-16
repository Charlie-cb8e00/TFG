
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
package com.geobus.paradas;

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

import java.math.BigDecimal;

@Entity
@Table(name = "parada")
public class Parada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "latitud", nullable = false)
    private BigDecimal latitud;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Column(name = "longitud", nullable = false)
    private BigDecimal longitud;

    @NotNull
    @Column(name = "linea_id", nullable = false)
    private Long lineaId;

    protected Parada() {
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
