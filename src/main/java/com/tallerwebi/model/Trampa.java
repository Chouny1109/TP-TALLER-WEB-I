package com.tallerwebi.model;

import javax.persistence.*;

@Entity
@Table(name = "Trampa")
public class Trampa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer valor;
    private String imagen;

    public Trampa() {}

    public Trampa(String nombre, Integer valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() { return nombre; }
    public Integer getValor() { return valor; }
    public String getImagen() { return imagen; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setValor(Integer valor) { this.valor = valor; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}
