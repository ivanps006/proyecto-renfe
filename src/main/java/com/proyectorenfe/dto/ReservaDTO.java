package com.proyectorenfe.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaDTO {

    private Long id;
    private Long viajeId;
    private String origen;
    private String destino;
    private LocalDate fecha;
    private LocalTime horaSalida;
    private LocalTime horaLlegada;
    private String asiento;
    private BigDecimal precio;
    private String estado;

    public ReservaDTO() {
    }

    public ReservaDTO(
            Long id,
            Long viajeId,
            String origen,
            String destino,
            LocalDate fecha,
            LocalTime horaSalida,
            LocalTime horaLlegada,
            String asiento,
            BigDecimal precio,
            String estado) {

        this.id = id;
        this.viajeId = viajeId;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.asiento = asiento;
        this.precio = precio;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public Long getViajeId() {
        return viajeId;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public LocalTime getHoraLlegada() {
        return horaLlegada;
    }

    public String getAsiento() {
        return asiento;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public String getEstado() {
        return estado;
    }
}