package com.proyectorenfe.dto;

import java.util.List;

public class VagonDTO {

    private int vagon;
    private int numeroAsientos;
    private int filas;
    private List<AsientoDTO> asientos;

    public VagonDTO() {
    }

    public VagonDTO(
            int vagon,
            int numeroAsientos,
            int filas,
            List<AsientoDTO> asientos) {

        this.vagon = vagon;
        this.numeroAsientos = numeroAsientos;
        this.filas = filas;
        this.asientos = asientos;
    }

    public int getVagon() {
        return vagon;
    }

    public void setVagon(int vagon) {
        this.vagon = vagon;
    }

    public int getNumeroAsientos() {
        return numeroAsientos;
    }

    public void setNumeroAsientos(int numeroAsientos) {
        this.numeroAsientos = numeroAsientos;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public List<AsientoDTO> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<AsientoDTO> asientos) {
        this.asientos = asientos;
    }
}