package com.tallerwebi.model;

import javax.persistence.*;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"partida_id", "orden"})
)
public class SiguientePreguntaSupervivencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "partida_id")
    private Partida partida;

    @ManyToOne
    @JoinColumn(name = "jugador_1_id")
    private Usuario jugador1;

    @ManyToOne
    @JoinColumn(name = "jugador_2_id")
    private Usuario jugador2;

    @ManyToOne
    @JoinColumn(name = "siguiente_pregunta_id")
    private Pregunta siguientePregunta;
    private Integer orden;
    public SiguientePreguntaSupervivencia() {}

    public SiguientePreguntaSupervivencia(Partida partida, Usuario jugador1, Usuario jugador2, Integer orden, Pregunta pregunta) {
        this.partida = partida;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.siguientePregunta = pregunta;
        this.orden = orden;


    }
    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }



    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public Pregunta getSiguientePregunta() {
        return siguientePregunta;
    }

    public void setSiguientePregunta(Pregunta siguientePregunta) {
        this.siguientePregunta = siguientePregunta;
    }

    public Usuario getJugador2() {
        return jugador2;
    }

    public void setJugador2(Usuario jugador2) {
        this.jugador2 = jugador2;
    }

    public Usuario getJugador1() {
        return jugador1;
    }

    public void setJugador1(Usuario jugador1) {
        this.jugador1 = jugador1;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
