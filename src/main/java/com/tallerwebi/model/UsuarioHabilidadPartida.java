package com.tallerwebi.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Usuario_Habilidad_Partida")
public class UsuarioHabilidadPartida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "habilidad_id")
    private Habilidad habilidad;

    @ManyToOne
    @JoinColumn(name = "partida_id")
    private Partida partida;

    public UsuarioHabilidadPartida() {
    }

    public UsuarioHabilidadPartida(Usuario usuario, Habilidad habilidad, Partida partida) {
        this.usuario = usuario;
        this.habilidad = habilidad;
        this.partida = partida;
        this.fecha = LocalDateTime.now();
    }
}
