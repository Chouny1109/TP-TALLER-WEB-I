package com.tallerwebi.model;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;

import javax.persistence.*;
import java.util.List;
<<<<<<< HEAD
import java.util.Objects;
=======
>>>>>>> c85b898 (WIP: cambios en PartidaController, Pregunta, Respuesta)

@Entity
public class Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String enunciado;
<<<<<<< HEAD

    @OneToMany(cascade = CascadeType.ALL)
    private List<Respuesta> respuesta;

    @Enumerated(EnumType.STRING)
    @Column(name = "Tipo_Pregunta")
    private TIPO_PREGUNTA tipoPregunta;

    public Pregunta(String enunciado, List<Respuesta> respuesta, TIPO_PREGUNTA tipoPregunta) {
        this.enunciado = enunciado;
        this.respuesta = respuesta;
=======

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Respuesta> respuestas;

    private CATEGORIA_PREGUNTA tipoPregunta;

    public Pregunta(String enunciado, List<Respuesta> respuestas, CATEGORIA_PREGUNTA tipoPregunta) {
        this.enunciado = enunciado;
        this.respuestas = respuestas;
>>>>>>> c85b898 (WIP: cambios en PartidaController, Pregunta, Respuesta)
        this.tipoPregunta = tipoPregunta;
    }

    public Pregunta() {

    }

    // Getters y setters
    public String getEnunciado() {
        return enunciado;
    }

<<<<<<< HEAD

    public TIPO_PREGUNTA getTipoPregunta(){
        return tipoPregunta;
    }

=======
    public List<Respuesta> respuestas() {
        return respuestas;
    }

>>>>>>> c85b898 (WIP: cambios en PartidaController, Pregunta, Respuesta)
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
