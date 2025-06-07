package com.tallerwebi.model;

import com.tallerwebi.dominio.enums.TIPO_PREGUNTA;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String enunciado;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Respuesta> respuesta;

    @Enumerated(EnumType.STRING)
    @Column(name = "Tipo_Pregunta")
    private TIPO_PREGUNTA tipoPregunta;

    public Pregunta(String enunciado, List<Respuesta> respuesta, TIPO_PREGUNTA tipoPregunta) {
        this.enunciado = enunciado;
        this.respuesta = respuesta;
        this.tipoPregunta = tipoPregunta;
    }

    public Pregunta() {

    }

    // Getters y setters
    public String getEnunciado() {
        return enunciado;
    }


    public TIPO_PREGUNTA getTipoPregunta(){
        return tipoPregunta;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
