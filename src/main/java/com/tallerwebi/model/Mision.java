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

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_tipoMision")// en que campo de la tabla actual se guarda la relacion hacia la otra entidad
    private TipoDeMision tipoMision;

    public Mision(String descripcion, Integer experiencia, Integer copas) {
        this.descripcion = descripcion;
        this.experiencia = experiencia;
        this.copas = copas;
        this.usuarios = new ArrayList<>();
    }

    public Mision() {

    }

    public Integer getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(Integer experiencia) {
        this.experiencia = experiencia;
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
}
