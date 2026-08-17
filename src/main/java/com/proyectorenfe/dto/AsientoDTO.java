package com.proyectorenfe.dto;

public class AsientoDTO {

    private String asiento;
    private boolean ocupado;

    public AsientoDTO() {
    }

    public AsientoDTO(String asiento, boolean ocupado) {
        this.asiento = asiento;
        this.ocupado = ocupado;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }
}
