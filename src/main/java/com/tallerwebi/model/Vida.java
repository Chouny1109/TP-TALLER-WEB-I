package com.tallerwebi.model;

public class Vida {
    private String nombre;
    private Integer valor;

    public Vida(String nombre, Integer valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() { return nombre; }
    public Integer getValor() { return valor; }
}
