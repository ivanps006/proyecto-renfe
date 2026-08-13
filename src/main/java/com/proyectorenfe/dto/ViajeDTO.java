package com.proyectorenfe.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ViajeDTO {

    private Long id;
    private String tren;
    private String origen;
    private String destino;
    private LocalDate fecha;
    private LocalTime horaSalida;
    private LocalTime horaLlegada;
    private BigDecimal precio;
    private String estado;

    public ViajeDTO() {
    }

    public ViajeDTO(
            Long id,
            String tren,
            String origen,
            String destino,
            LocalDate fecha,
            LocalTime horaSalida,
            LocalTime horaLlegada,
            BigDecimal precio,
            String estado) {

        this.id = id;
        this.tren = tren;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.precio = precio;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getTren() {
        return tren;
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public String getEstado() {
        return estado;
    }
}