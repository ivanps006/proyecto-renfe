package com.proyectorenfe.controller;

import com.proyectorenfe.dto.ViajeDTO;
import com.proyectorenfe.model.Viaje;
import com.proyectorenfe.repository.ViajeRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/viajes")
public class ViajeController {

    private final ViajeRepository viajeRepository;

    public ViajeController(ViajeRepository viajeRepository) {
        this.viajeRepository = viajeRepository;
    }

    @GetMapping
    public List<Viaje> obtenerTodos() {
        return viajeRepository.findAll();
    }

    @GetMapping("/buscar")
    public List<ViajeDTO> buscarViajes(
            @RequestParam Long origen,
            @RequestParam Long destino,
            @RequestParam String fecha
    ) {
        LocalDate fechaViaje = LocalDate.parse(fecha);

        List<Viaje> viajes = viajeRepository.findByRutaOrigenIdAndRutaDestinoIdAndFecha(
                origen,
                destino,
                fechaViaje
        );

        return viajes.stream().map(
                viaje -> new ViajeDTO(
                        viaje.getId(),
                        viaje.getTren().getNumero(),
                        viaje.getRuta().getOrigen().getNombre(),
                        viaje.getRuta().getDestino().getNombre(),
                        viaje.getFecha(),
                        viaje.getHoraSalida(),
                        viaje.getHoraLlegada(),
                        viaje.getPrecio(),
                        viaje.getEstado()
                ))
                .toList();
    }
}