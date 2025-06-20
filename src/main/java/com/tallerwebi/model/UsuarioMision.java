package com.tallerwebi.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "usuario_mision")
public class UsuarioMision {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "mision_id")
    private Mision mision;

    @NotNull
    @Column(nullable = false)
    private Boolean canjeada;

    @NotNull
    @Column(nullable = false)
    private Boolean completada;

    @NotNull
    private LocalDate fechaDeAsignacion;

    public UsuarioMision() {
    }

    public UsuarioMision(Usuario usuario, Mision mision) {
        this.usuario = usuario;
        this.mision = mision;
        this.canjeada = false;
        this.completada = false;
        this.fechaDeAsignacion = LocalDate.now();
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
