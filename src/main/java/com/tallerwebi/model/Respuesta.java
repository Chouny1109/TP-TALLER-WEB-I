package com.tallerwebi.model;

<<<<<<< HEAD

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
=======
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
>>>>>>> c85b898 (WIP: cambios en PartidaController, Pregunta, Respuesta)

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
