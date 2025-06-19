package com.tallerwebi.model;

import com.tallerwebi.dominio.enums.TIPO_MISION;

import javax.persistence.*;

@Entity
public class TipoDeMision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TIPO_MISION nombre;

    public TipoDeMision() {
    }

    public TipoDeMision(TIPO_MISION nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TIPO_MISION getNombre() {
        return nombre;
    }

    public void setNombre(TIPO_MISION nombre) {
        this.nombre = nombre;
    }
}
