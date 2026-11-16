package com.geobus.usuarios_paradas;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UsuarioParadaId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "parada_id")
    private Long paradaId;

    protected UsuarioParadaId() {
    }

    public UsuarioParadaId(Long usuarioId, Long paradaId) {
        this.usuarioId = usuarioId;
        this.paradaId = paradaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getParadaId() {
        return paradaId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof UsuarioParadaId that)) return false;
        return Objects.equals(usuarioId, that.usuarioId)
                && Objects.equals(paradaId, that.paradaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, paradaId);
    }
}
