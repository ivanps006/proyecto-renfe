package com.proyectorenfe.controller;

import com.proyectorenfe.dto.CrearReservaDTO;
import com.proyectorenfe.dto.ReservaDTO;
import com.proyectorenfe.model.Reserva;
import com.proyectorenfe.model.Usuario;
import com.proyectorenfe.model.Viaje;
import com.proyectorenfe.repository.ReservasRepository;
import com.proyectorenfe.repository.UsuarioRepository;
import com.proyectorenfe.repository.ViajeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservasRepository reservasRepository;
    private final UsuarioRepository usuarioRepository;
    private final ViajeRepository viajeRepository;

    public ReservaController(
            ReservasRepository reservasRepository,
            UsuarioRepository usuarioRepository,
            ViajeRepository viajeRepository) {

        this.reservasRepository = reservasRepository;
        this.usuarioRepository = usuarioRepository;
        this.viajeRepository = viajeRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaDTO crearReserva(
            @RequestBody CrearReservaDTO datos,
            Authentication authentication) {

        Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuario no encontrado"
                ));

        Viaje viaje = viajeRepository.findById(datos.getViajeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Viaje no encontrado"
                ));

        if (reservasRepository.existsByViajeAndAsiento(viaje, datos.getAsiento())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ese asiento ya está ocupado"
            );
        }

        Reserva reserva = new Reserva(
                usuario,
                viaje,
                datos.getAsiento(),
                viaje.getPrecio()
        );

        Reserva reservaGuardada = reservasRepository.save(reserva);

        return convertirAReservaDTO(reservaGuardada);
    }

    @GetMapping("/mis-reservas")
    public List<ReservaDTO> obtenerMisReservas(Authentication authentication) {

        Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuario no encontrado"
                ));

        return reservasRepository.findByUsuario(usuario)
                .stream()
                .map(this::convertirAReservaDTO)
                .toList();
    }

    private ReservaDTO convertirAReservaDTO(Reserva reserva) {
        Viaje viaje = reserva.getViaje();

        return new ReservaDTO(
                reserva.getId(),
                viaje.getId(),
                viaje.getRuta().getOrigen().getNombre(),
                viaje.getRuta().getDestino().getNombre(),
                viaje.getFecha(),
                viaje.getHoraSalida(),
                viaje.getHoraLlegada(),
                reserva.getAsiento(),
                reserva.getPrecio(),
                reserva.getEstado().name()
        );
    }
}