package com.geobus.usuarios_paradas;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario_parada")
public class UsuarioParada {

    @EmbeddedId
    private UsuarioParadaId id;

    @Column(name = "favorito", nullable = false)
    private boolean favorito;

    protected UsuarioParada() {
    }

    public UsuarioParada(Long usuarioId, Long paradaId, boolean favorito) {
        this.id = new UsuarioParadaId(usuarioId, paradaId);
        this.favorito = favorito;
    }

    public UsuarioParadaId getId() {
        return id;
    }

    public boolean isFavorito() {
        return favorito;
    }
}
