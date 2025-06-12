package com.tallerwebi.model;

import javax.persistence.*;

@Entity
public class UsuarioPartida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partida_id")
    private Partida partida;


    public UsuarioPartida() {}
    public UsuarioPartida(Usuario usuario, Partida partida) {
        this.usuario = usuario;
        this.partida = partida;
    }
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
    public void setPartida(Partida partida) {

        this.partida = partida;
    }
    public Partida getPartida() {
        return partida;
    }
}
