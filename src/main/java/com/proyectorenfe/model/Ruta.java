package com.proyectorenfe.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "rutas")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "origen_id", nullable = false)
    private Estacion origen;

    @ManyToOne
    @JoinColumn(name = "destino_id", nullable = false)
    private Estacion destino;

    @Column(nullable = false)
    private BigDecimal distancia;

    public Ruta() {
    }

    public Ruta(Estacion origen, Estacion destino, BigDecimal distancia) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
    }

    public Long getId() {
        return id;
    }

    public Estacion getOrigen() {
        return origen;
    }

    public void setOrigen(Estacion origen) {
        this.origen = origen;
    }

    public Estacion getDestino() {
        return destino;
    }

    public void setDestino(Estacion destino) {
        this.destino = destino;
    }

    public BigDecimal getDistancia() {
        return distancia;
    }

    public void setDistancia(BigDecimal distancia) {
        this.distancia = distancia;
    }
}