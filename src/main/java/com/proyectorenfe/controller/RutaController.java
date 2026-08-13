package com.proyectorenfe.controller;

import com.proyectorenfe.model.Ruta;
import com.proyectorenfe.repository.RutaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rutas")
public class RutaController {

    private final RutaRepository rutaRepository;

    public RutaController(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    @GetMapping
    public List<Ruta> obtenerTodas() {
        return rutaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Ruta obtenerPorId(@PathVariable Long id) {
        return rutaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));
    }

    @PostMapping
    public Ruta crearRuta(@RequestBody Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    @PutMapping("/{id}")
    public Ruta actualizarRuta(
            @PathVariable Long id,
            @RequestBody Ruta rutaActualizada) {

        Ruta ruta = rutaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));

        ruta.setOrigen(rutaActualizada.getOrigen());
        ruta.setDestino(rutaActualizada.getDestino());
        ruta.setDistancia(rutaActualizada.getDistancia());

        return rutaRepository.save(ruta);
    }

    @DeleteMapping("/{id}")
    public String eliminarRuta(@PathVariable Long id) {

        if (!rutaRepository.existsById(id)) {
            throw new RuntimeException("Ruta no encontrada");
        }

        rutaRepository.deleteById(id);

        return "Ruta eliminada correctamente";
    }
}