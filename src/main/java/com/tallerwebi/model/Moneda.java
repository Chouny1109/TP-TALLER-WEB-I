package com.tallerwebi.model;

import javax.persistence.*;

@Entity
@Table(name = "Moneda")
public class Moneda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer valor;
    private Integer cantidad;

    public Moneda() {}

    public Moneda(String nombre, Integer valor) {
        this.nombre = nombre;
        this.valor = valor;
    }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Integer getValor() { return valor; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setValor(Integer valor) { this.valor = valor; }
    public Integer getCantidad() { return cantidad; }
}
