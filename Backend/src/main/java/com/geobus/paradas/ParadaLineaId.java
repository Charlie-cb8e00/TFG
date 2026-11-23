
package com.geobus.paradas;

import java.io.Serializable;
import java.util.Objects;

public class ParadaLineaId implements Serializable {

    private Integer paradaId;
    private Integer lineaId;

    public ParadaLineaId() {
    }

    public ParadaLineaId(Integer paradaId, Integer lineaId) {
        this.paradaId = paradaId;
        this.lineaId = lineaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParadaLineaId)) return false;
        ParadaLineaId that = (ParadaLineaId) o;
        return Objects.equals(paradaId, that.paradaId)
                && Objects.equals(lineaId, that.lineaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paradaId, lineaId);
    }
}
