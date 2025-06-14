package com.tallerwebi.model;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;

import javax.persistence.*;
import java.util.List;

@Entity
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String enunciado;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Respuesta> respuestas;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipoPregunta")
    private CATEGORIA_PREGUNTA tipoPregunta;

    public Pregunta(String enunciado, List<Respuesta> respuestas, CATEGORIA_PREGUNTA tipoPregunta) {
        this.enunciado = enunciado;
        this.respuestas = respuestas;
        this.tipoPregunta = tipoPregunta;
    }

    public Pregunta() {

    }

    // Getters y setters
    public String getEnunciado() {
        return enunciado;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
