package com.proyectorenfe.dto;

public class CrearReservaDTO {
    private Long viajeId;
    private String asiento;

    public CrearReservaDTO() {
    }

    public CrearReservaDTO(Long viajeId, String asiento) {
        this.viajeId = viajeId;
        this.asiento = asiento;
    }

    public Long getViajeId() {
        return viajeId;
    }

    public void setViajeId(Long viajeId) {
        this.viajeId = viajeId;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }
}
