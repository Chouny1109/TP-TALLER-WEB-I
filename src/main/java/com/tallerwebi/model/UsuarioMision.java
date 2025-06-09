package com.tallerwebi.model;

import javax.persistence.*;

@Entity
public class UsuarioMision {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "mision_id")
    private Mision mision;

    private Boolean canjeada;

    private Boolean completada;

    public UsuarioMision() {
    }

    public UsuarioMision(Long id, Usuario usuario, Mision mision, Boolean canjeada, Boolean completada) {
        this.id = id;
        this.usuario = usuario;
        this.mision = mision;
        this.canjeada = canjeada;
        this.completada = completada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Mision getMision() {
        return mision;
    }

    public void setMision(Mision mision) {
        this.mision = mision;
    }

    public Boolean getCanjeada() {
        return canjeada;
    }

    public void setCanjeada(Boolean canjeada) {
        this.canjeada = canjeada;
    }

    public Boolean getCompletada() {
        return completada;
    }

    public void setCompletada(Boolean completada) {
        this.completada = completada;
    }
}
