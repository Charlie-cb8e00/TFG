package com.geobus.lineas_autobuses;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "linea_autobus")
public class LineaAutobus {

    @EmbeddedId
    private LineaAutobusId id;

    protected LineaAutobus() {
    }

    public LineaAutobus(Long lineaId, Long autobusId) {
        this.id = new LineaAutobusId(lineaId, autobusId);
    }

    public LineaAutobusId getId() {
        return id;
    }
}
