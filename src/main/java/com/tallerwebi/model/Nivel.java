package com.tallerwebi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Nivel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer nivel;

    private Integer experienciaNecesaria;

    public Nivel() {
    }

    public Nivel(Integer nivel, Integer experienciaNecesaria) {
        this.nivel = nivel;
        this.experienciaNecesaria = experienciaNecesaria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Integer getExperienciaNecesaria() {
        return experienciaNecesaria;
    }

    public void setExperienciaNecesaria(Integer experienciaNecesaria) {
        this.experienciaNecesaria = experienciaNecesaria;
    }
}
