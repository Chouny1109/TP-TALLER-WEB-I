package com.tallerwebi.model;


import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer valor;

    @ManyToOne(cascade = CascadeType.ALL)
    private Habilidad Habilidad;
    private Boolean estado = false;

    public Avatar(String nombre, Integer valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public Avatar() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
