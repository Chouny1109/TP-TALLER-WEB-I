package com.tallerwebi.model;

import javax.persistence.*;

@Entity
@Table(name = "Amigo")
public class Amigo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String imagen;


    public Amigo() {
    }


    public Amigo(String nombre, String imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
