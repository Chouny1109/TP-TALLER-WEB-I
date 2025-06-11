package com.tallerwebi.model;

import javax.persistence.*;

@Entity
@Table(name = "trampa_usuario")
public class TrampaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "trampa_id", nullable = false)
    private Trampa trampa;

    private int cantidad;

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setTrampa(Trampa trampa) {
        this.trampa = trampa;
    }
    public Trampa getTrampa() {
        return trampa;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}

