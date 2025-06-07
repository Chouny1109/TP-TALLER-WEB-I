package com.tallerwebi.model;

import javax.persistence.*;

@Entity
@Table(name = "Avatar")
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer valor;

    @ManyToOne(cascade = CascadeType.ALL)
    private Habilidad Habilidad;
    private Boolean estado = false;

    public Avatar(String nombre, Integer valor) {
        this.valor = valor;
        this.nombre = nombre;
    }

    public Avatar() {
        
    }

    public String getNombre() { return nombre; }
    public Integer getValor() { return valor; }
    public Boolean getEstado() { return estado; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setValor(Integer valor) { this.valor = valor; }
    public void setEstado(Boolean estado) { this.estado = estado; }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
