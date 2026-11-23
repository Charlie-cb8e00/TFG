
package com.geobus.paradas;

import jakarta.persistence.*;

@Entity
@Table(name = "parada_linea")
@IdClass(ParadaLineaId.class)
public class ParadaLinea {

    @Id
    @Column(name = "parada_id")
    private Integer paradaId;

    @Id
    @Column(name = "linea_id")
    private Integer lineaId;

    public ParadaLinea() {
    }

    public Integer getParadaId() {
        return paradaId;
    }

    public Integer getLineaId() {
        return lineaId;
    }
}
