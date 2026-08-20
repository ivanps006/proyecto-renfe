package com.proyectorenfe.repository;

import com.proyectorenfe.model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    List<Viaje> findByRutaOrigenIdAndRutaDestinoIdAndFecha(
            Long origenId,
            Long destinoId,
            LocalDate fecha
    );
}
