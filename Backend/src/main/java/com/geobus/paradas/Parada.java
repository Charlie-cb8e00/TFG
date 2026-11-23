
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

    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
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

    public Long getLineaId() {
        return lineaId;
    }
}
