package com.tallerwebi.model;

import javax.persistence.*;

@Entity
@Table(name = "Vida")
public class Vida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer valor;

    public Vida() {}

    public Vida(String nombre, Integer valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() { return nombre; }
    public Integer getValor() { return valor; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setValor(Integer valor) { this.valor = valor; }
}