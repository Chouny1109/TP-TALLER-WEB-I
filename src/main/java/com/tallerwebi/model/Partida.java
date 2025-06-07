package com.tallerwebi.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean estadoPartida;

    @ManyToMany
    private List<Pregunta> preguntas;
    @ManyToMany
    private List<Usuario> usuarios;

    public Partida(Boolean estadoPartida) {
        this.estadoPartida = estadoPartida;
    }

    public Partida() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
