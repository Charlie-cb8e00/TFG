package com.geobus.lineas_autobuses;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LineaAutobusId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "linea_id")
    private Long lineaId;

    @Column(name = "autobus_id")
    private Long autobusId;

    protected LineaAutobusId() {
    }

    public LineaAutobusId(Long lineaId, Long autobusId) {
        this.lineaId = lineaId;
        this.autobusId = autobusId;
    }

    public Long getLineaId() {
        return lineaId;
    }

    public Long getAutobusId() {
        return autobusId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof LineaAutobusId that)) return false;
        return Objects.equals(lineaId, that.lineaId)
                && Objects.equals(autobusId, that.autobusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineaId, autobusId);
    }
}
