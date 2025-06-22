package com.tallerwebi.model;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String enunciado;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Respuesta> respuestas;



    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tipoPregunta")
    private CATEGORIA_PREGUNTA tipoPregunta;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL)
    private Set<UsuarioRespondePregunta> respuestasUsuarios;


    public Pregunta(String enunciado, List<Respuesta> respuestas, CATEGORIA_PREGUNTA tipoPregunta) {
        this.enunciado = enunciado;
        this.respuestas = respuestas;
        this.tipoPregunta = tipoPregunta;
    }

    public Pregunta() {

    }
    public CATEGORIA_PREGUNTA getTipoPregunta() {
        return tipoPregunta;
    }

    public void setTipoPregunta(CATEGORIA_PREGUNTA tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
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
    public void setRespuestasUsuarios(Set<UsuarioRespondePregunta> respuestasUsuarios) {
        this.respuestasUsuarios = respuestasUsuarios;
    }
    public Set<UsuarioRespondePregunta> getRespuestasUsuarios() {
        return respuestasUsuarios;
    }
}
