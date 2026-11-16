package com.geobus.monederos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "monedero")
public class Monedero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank
    @Column(name = "tipo_tarjeta", nullable = false)
    private String tipoTarjeta;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "saldo", nullable = false)
    private BigDecimal saldo;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    protected Monedero() {
    }

    public Monedero(String nombre, String tipoTarjeta, BigDecimal saldo, Long usuarioId) {
        this.nombre = nombre;
        this.tipoTarjeta = tipoTarjeta;
        this.saldo = saldo;
        this.usuarioId = usuarioId;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }
}
