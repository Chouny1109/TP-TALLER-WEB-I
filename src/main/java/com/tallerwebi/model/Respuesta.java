package com.tallerwebi.model;

import javax.persistence.*;

@Entity
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String descripcion;

    private Boolean opcionCorrecta;



    @ManyToOne
    @JoinColumn(name = "pregunta_id")
    private Pregunta pregunta;


    public Boolean getOpcionCorrecta() {
        return opcionCorrecta;
    }

    public void setOpcionCorrecta(Boolean opcionCorrecta) {
        this.opcionCorrecta = opcionCorrecta;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
