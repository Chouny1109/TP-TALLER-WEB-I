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
    private Integer cantidad;

    public Vida() {}

    public Vida(String nombre, Integer valor, Integer cantidad) {
        this.nombre = nombre;
        this.valor = valor;
        this.cantidad = cantidad;
    }

    public String getNombre() { return nombre; }
    public Integer getValor() { return valor; }
    public Integer getCantidad() { return cantidad; }
    public Long getId() { return id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setValor(Integer valor) { this.valor = valor; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}