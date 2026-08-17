package com.proyectorenfe.repository;

import com.proyectorenfe.model.Reserva;
import com.proyectorenfe.model.Usuario;
import com.proyectorenfe.model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservasRepository extends JpaRepository<Reserva, Long> {
    boolean existsByViajeAndAsiento(Viaje viaje, String asiento);

    List<Reserva> findByUsuario(Usuario usuario);

    List<Reserva> findByViaje(Viaje viaje);
}
