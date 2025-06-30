package com.tallerwebi.dominio.enums;

public enum TIPO_MISION {
    GANAR_PARTIDAS("Ganar partidas"),
    JUGAR_PARTIDAS("Jugar partidas"),
    USAR_HABILIDADES("Usar habilidades"),
    INICIAR_SESION("Iniciar sesion"),
    NO_USAR_HABILIDADES("No usar habilidades"),
    GANAR_PARTIDA_CONSECUTIVA("Ganar partidas consecutivas");

    private final String descripcion;

    TIPO_MISION(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
