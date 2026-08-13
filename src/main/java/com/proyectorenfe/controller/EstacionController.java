package com.proyectorenfe.controller;

import com.proyectorenfe.model.Estacion;
import com.proyectorenfe.repository.EstacionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estaciones")
public class EstacionController {

    private final EstacionRepository estacionRepository;

    public EstacionController(EstacionRepository estacionRepository) {
        this.estacionRepository = estacionRepository;
    }

    @GetMapping
    public List<Estacion> obtenerTodas() {
        return estacionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Estacion obtenerPorId(@PathVariable Long id) {
        return estacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estación no encontrada"));
    }

    @PostMapping
    public Estacion crearEstacion(@RequestBody Estacion estacion) {
        return estacionRepository.save(estacion);
    }

    @PutMapping("/{id}")
    public Estacion actualizarEstacion(
            @PathVariable Long id,
            @RequestBody Estacion estacionActualizada) {

        Estacion estacion = estacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estación no encontrada"));

        estacion.setNombre(estacionActualizada.getNombre());
        estacion.setCiudad(estacionActualizada.getCiudad());

        return estacionRepository.save(estacion);
    }

    @DeleteMapping("/{id}")
    public String eliminarEstacion(@PathVariable Long id) {

        if (!estacionRepository.existsById(id)) {
            throw new RuntimeException("Estación no encontrada");
        }

        estacionRepository.deleteById(id);

        return "Estación eliminada correctamente";
    }
}