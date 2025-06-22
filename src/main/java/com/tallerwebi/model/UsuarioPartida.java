package com.tallerwebi.model;

import com.tallerwebi.dominio.enums.ESTADO_PARTIDA;
import com.tallerwebi.dominio.enums.ESTADO_PARTIDA_JUGADOR;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private ESTADO_PARTIDA_JUGADOR estado;

    public UsuarioPartida() {

    }

    public UsuarioPartida(Usuario usuario, Partida partida) {
        this.usuario = usuario;
        this.partida = partida;
        this.fecha = LocalDateTime.now();
        this.estado = ESTADO_PARTIDA_JUGADOR.PENDIENTE;
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

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public ESTADO_PARTIDA_JUGADOR getEstado() {
        return estado;
    }

    public void setEstado(ESTADO_PARTIDA_JUGADOR estado) {
        this.estado = estado;
    }
}
