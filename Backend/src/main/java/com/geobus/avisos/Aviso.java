package com.geobus.avisos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(
        name = "aviso",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_aviso_usuario_parada_tiempo",
                columnNames = {"usuario_id", "parada_id", "tiempo"}))
public class Aviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Min(1)
    @Max(120)
    @Column(name = "tiempo", nullable = false)
    private int tiempo;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "parada_id", nullable = false)
    private Long paradaId;

    protected Aviso() {
    }

    public Aviso(String nombre, int tiempo, Long usuarioId, Long paradaId) {
        this.nombre = nombre;
        this.tiempo = tiempo;
        this.usuarioId = usuarioId;
        this.paradaId = paradaId;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTiempo() {
        return tiempo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getParadaId() {
        return paradaId;
    }
}
