
package com.geobus.paradas;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paradas")
@CrossOrigin(origins = "*")
public class ParadaController {

    private final ParadaRepository paradaRepository;

    public ParadaController(ParadaRepository paradaRepository) {
        this.paradaRepository = paradaRepository;
    }

    @GetMapping
    public List<Parada> obtenerParadas() {
        return paradaRepository.findAll();
    }
}
