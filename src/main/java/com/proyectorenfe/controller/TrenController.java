package com.proyectorenfe.controller;


import com.proyectorenfe.model.Tren;
import com.proyectorenfe.repository.TrenRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trenes")
public class TrenController {

    private final TrenRepository trenRepository;

    public TrenController(TrenRepository trenRepository){
        this.trenRepository = trenRepository;
    }

    @GetMapping
    public List<Tren> obtenerTodos(){
        return trenRepository.findAll();
    }

    @GetMapping("/{id}")
    public Tren obtenerPorId(@PathVariable Long id){
        return trenRepository.findById(id).orElseThrow(() -> new RuntimeException("Tren no encontrado"));
    }

    @PostMapping
    public Tren crearTren(@RequestBody Tren tren){
        return trenRepository.save(tren);
    }

    @PutMapping("/{id}")
    public Tren actualizarTren(@PathVariable Long id, @RequestBody Tren trenActualizado) {

        Tren tren = trenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tren no encontrado"));

        tren.setNumero(trenActualizado.getNumero());
        tren.setModelo(trenActualizado.getModelo());
        tren.setCapacidad(trenActualizado.getCapacidad());
        tren.setEstado(trenActualizado.getEstado());

        return trenRepository.save(tren);
    }

    @DeleteMapping("/{id}")
    public String eliminarTren(@PathVariable Long id) {

        if (!trenRepository.existsById(id)) {
            throw new RuntimeException("Tren no encontrado");
        }

        trenRepository.deleteById(id);

        return "Tren eliminado correctamente";
    }

}
