package com.tallerwebi.model;

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

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "mision_id")
    private Mision mision;

    @Column(nullable = false)
    private Boolean canjeada;

    private Integer progreso;

    @Column(nullable = false)
    private Boolean completada;

    private LocalDate fechaDeAsignacion;

    public UsuarioMision() {
    }

    public UsuarioMision(Usuario usuario, Mision mision) {
        this.usuario = usuario;
        this.mision = mision;
        this.canjeada = false;
        this.completada = false;
        this.progreso = 0;
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

    public Integer getProgreso() {
        return progreso;
    }

    public void setProgreso(Integer progreso) {
        this.progreso = progreso;
    }

    public LocalDate getFechaDeAsignacion() {
        return fechaDeAsignacion;
    }

    public void setFechaDeAsignacion(LocalDate fechaDeAsignacion) {
        this.fechaDeAsignacion = fechaDeAsignacion;
    }
}
