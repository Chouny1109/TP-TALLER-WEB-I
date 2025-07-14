package com.tallerwebi.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Mision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String descripcion;

    @NotNull
    private Integer experiencia;

    @NotNull
    private Integer copas;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mision")
    private List<UsuarioMision> usuarios;

    private Integer cantidad;

    private Integer objetivo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_tipoMision")// en que campo de la tabla actual se guarda la relacion hacia la otra entidad
    private TipoDeMision tipoMision;

    public Mision(String descripcion, Integer experiencia, Integer copas, Integer cantidad, Integer objetivo) {
        this.descripcion = descripcion;
        this.experiencia = experiencia;
        this.copas = copas;
        this.cantidad = cantidad;
        this.usuarios = new ArrayList<>();
        this.objetivo = objetivo;
    }

    public Mision() {

    }

    public Integer getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(Integer experiencia) {
        this.experiencia = experiencia;
    }

    public Integer getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Integer objetivo) {
        this.objetivo = objetivo;
    }

    public Integer getCopas() {
        return copas;
    }

    public void setCopas(Integer copas) {
        this.copas = copas;
    }

    public List<UsuarioMision> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuarioMision> usuarios) {
        this.usuarios = usuarios;
    }

    public TipoDeMision getTipoMision() {
        return tipoMision;
    }

    public void setTipoMision(TipoDeMision tipoMision) {
        this.tipoMision = tipoMision;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
