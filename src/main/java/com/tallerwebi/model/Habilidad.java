package com.tallerwebi.model;

import com.tallerwebi.dominio.enums.TIPO_HABILIDAD;

import javax.persistence.*;

@Entity
public class Habilidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "Tipo_Habilidad")
    private TIPO_HABILIDAD tipoHabilidad;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
