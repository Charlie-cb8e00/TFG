
package com.geobus.paradas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParadaLineaRepository
        extends JpaRepository<ParadaLinea, ParadaLineaId> {

    List<ParadaLinea> findByParadaId(Integer paradaId);

    List<ParadaLinea> findByLineaId(Integer lineaId);
}
