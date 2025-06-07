package com.tallerwebi.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Pregunta pregunta;

    private String descripcion;
    private Boolean opcionCorrecta;

    public Respuesta(){

    }
    public Respuesta(String descripcion, Boolean opcionCorrecta){
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
