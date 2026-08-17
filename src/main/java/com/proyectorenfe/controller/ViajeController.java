package com.proyectorenfe.controller;

import com.proyectorenfe.dto.AsientoDTO;
import com.proyectorenfe.dto.VagonDTO;
import com.proyectorenfe.dto.ViajeDTO;
import com.proyectorenfe.model.EstadoReserva;
import com.proyectorenfe.model.Reserva;
import com.proyectorenfe.model.Viaje;
import com.proyectorenfe.repository.ReservasRepository;
import com.proyectorenfe.repository.ViajeRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/viajes")
public class ViajeController {

    private final ViajeRepository viajeRepository;
    private final ReservasRepository reservasRepository;

    public ViajeController(
            ViajeRepository viajeRepository,
            ReservasRepository reservasRepository) {

        this.viajeRepository = viajeRepository;
        this.reservasRepository = reservasRepository;
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

        List<Viaje> viajes =
                viajeRepository.findByRutaOrigenIdAndRutaDestinoIdAndFecha(
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

    @GetMapping("/{id}/asientos")
    public List<VagonDTO> obtenerAsientos(@PathVariable Long id) {

        // 1. Buscar el viaje
        Viaje viaje = viajeRepository.findById(id)
                .orElseThrow();

        // 2. Obtener las reservas de ese viaje
        List<Reserva> reservas =
                reservasRepository.findByViaje(viaje);

        // 3. Guardar los asientos ocupados
        Set<String> asientosOcupados = new HashSet<>();

        for (Reserva reserva : reservas) {

            if (reserva.getEstado() == EstadoReserva.CONFIRMADA) {
                asientosOcupados.add(reserva.getAsiento());
            }
        }

        // 4. Obtener configuración del tren
        int numeroVagones = viaje.getTren().getNumeroVagones();
        int asientosPorVagon = viaje.getTren().getAsientosPorVagon();

        // 5. Calcular filas
        int filasPorVagon = asientosPorVagon / 4;

        // 6. Crear lista de vagones
        List<VagonDTO> vagones = new ArrayList<>();

        // 7. Generar cada vagón
        for (int vagon = 1; vagon <= numeroVagones; vagon++) {

            List<AsientoDTO> asientos = new ArrayList<>();

            // 8. Generar los asientos del vagón
            for (int fila = 1; fila <= filasPorVagon; fila++) {

                String[] letras = {"A", "B", "C", "D"};

                for (String letra : letras) {

                    String numeroAsiento =
                            "V" + vagon + "-" + fila + letra;

                    boolean ocupado =
                            asientosOcupados.contains(numeroAsiento);

                    asientos.add(
                            new AsientoDTO(
                                    numeroAsiento,
                                    ocupado
                            )
                    );
                }
            }

            // 9. Añadir el vagón completo
            vagones.add(
                    new VagonDTO(
                            vagon,
                            asientosPorVagon,
                            filasPorVagon,
                            asientos
                    )
            );
        }

        // 10. Devolver todos los vagones
        return vagones;
    }
}