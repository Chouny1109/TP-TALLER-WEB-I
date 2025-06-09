package com.tallerwebi.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Mision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private Integer experiencia;

    private Integer copas;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mision")
    private List<UsuarioMision> usuarios;

    public Mision(String descripcion, Integer experiencia, Integer copas) {
        this.descripcion = descripcion;
        this.experiencia = experiencia;
        this.copas = copas;
        this.usuarios = new ArrayList<>();
    }

    public Mision() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
