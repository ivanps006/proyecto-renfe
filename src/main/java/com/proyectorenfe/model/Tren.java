package com.proyectorenfe.model;

import jakarta.persistence.*;

@Entity
@Table(name="trenes")
public class Tren {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private int capacidad;

    @Column(nullable = false)
    private String estado;

    Tren(){

    }

    public Tren(Long id, String numero, String modelo, int capacidad, String estado) {
        this.id = id;
        this.numero = numero;
        this.modelo = modelo;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}