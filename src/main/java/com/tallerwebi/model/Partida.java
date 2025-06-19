package com.tallerwebi.model;

import com.tallerwebi.dominio.enums.ESTADO_AVATAR;
import com.tallerwebi.dominio.enums.ESTADO_PARTIDA;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "estadoPartida")
    private ESTADO_PARTIDA estadoPartida = ESTADO_PARTIDA.ABIERTA;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoPartida")
    private TIPO_PARTIDA tipo;

    @ManyToMany
    private List<Pregunta> preguntas;

    public Partida(ESTADO_PARTIDA estadoPartida) {
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

    public TIPO_PARTIDA getTipo() {
        return tipo;
    }

    public void setTipo(TIPO_PARTIDA tipo) {
        this.tipo = tipo;
    }

    public ESTADO_PARTIDA getEstadoPartida() {
        return estadoPartida;
    }

    public void setEstadoPartida(ESTADO_PARTIDA estadoPartida) {
        this.estadoPartida = estadoPartida;
    }
    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }
}
