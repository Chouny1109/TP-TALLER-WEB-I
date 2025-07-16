package com.tallerwebi.model;

public class NivelUsuarioDTO {
    private final Integer nivelActual;
    private final Integer nivelSiguiente;
    private final Integer experienciaActual;
    private final Integer experienciaNecesaria;
    private final Integer restante;

    public NivelUsuarioDTO(Integer nivelActual, Integer nivelSiguiente, Integer experienciaActual, Integer experienciaNecesaria, Integer restante) {
        this.nivelActual = nivelActual;
        this.nivelSiguiente = nivelSiguiente;
        this.experienciaActual = experienciaActual;
        this.experienciaNecesaria = experienciaNecesaria;
        this.restante = restante;
    }

    public Integer getNivelActual() {
        return nivelActual;
    }

    public Integer getNivelSiguiente() {
        return nivelSiguiente;
    }

    public Integer getExperienciaActual() {
        return experienciaActual;
    }

    public Integer getExperienciaNecesaria() {
        return experienciaNecesaria;
    }

    public Integer getRestante() {
        return restante;
    }

}
