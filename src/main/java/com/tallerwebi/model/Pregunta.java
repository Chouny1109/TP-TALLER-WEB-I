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

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL)
    private List<UsuarioRespondePregunta> respuestasUsuarios;


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
    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }
    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }
    public List<Respuesta> getRespuestas() {
        return respuestas;
    }
    public void setRespuestasUsuarios(List<UsuarioRespondePregunta> respuestasUsuarios) {
        this.respuestasUsuarios = respuestasUsuarios;
    }
    public List<UsuarioRespondePregunta> getRespuestasUsuarios() {
        return respuestasUsuarios;
    }
}
