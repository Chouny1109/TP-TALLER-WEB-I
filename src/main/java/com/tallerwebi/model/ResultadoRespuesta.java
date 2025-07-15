package com.tallerwebi.model;

import javax.persistence.*;
import javax.swing.text.StyledEditorKit;

@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"partida_id", "usuario_id", "orden", "pregunta_id"})
)
@Entity
public class ResultadoRespuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "pregunta_id")
    private Pregunta pregunta;

    public Integer getXpEnTurno() {
        return xpEnTurno;
    }

    public void setXpEnTurno(Integer xpEnTurno) {
        this.xpEnTurno = xpEnTurno;
    }

    private Integer xpEnTurno = 0;

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }



    private Integer orden;

    @ManyToOne
    @JoinColumn(name = "respuesta_correcta_id")
    private Respuesta respuestaCorrecta;
    @ManyToOne
    @JoinColumn(name = "respuesta_seleccionada_id")
    private Respuesta respuestaSeleccionada;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "partida_id")
    private Partida partida;

    public Boolean getTiempoTerminadoRespuestaNula() {
        return tiempoTerminadoRespuestaNula;
    }

    public void setTiempoTerminadoRespuestaNula(Boolean tiempoTerminadoRespuestaNula) {
        this.tiempoTerminadoRespuestaNula = tiempoTerminadoRespuestaNula;
    }

    private Boolean tiempoTerminadoRespuestaNula;

    public Respuesta getRespuestaSeleccionada() {
        return respuestaSeleccionada;
    }

    public void setRespuestaSeleccionada(Respuesta respuestaSeleccionada) {
        this.respuestaSeleccionada = respuestaSeleccionada;
    }

    public Respuesta getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(Respuesta respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }


    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }
    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public ResultadoRespuesta() {

    }

    public boolean respondioBien() {
        return respuestaSeleccionada != null &&
                respuestaCorrecta != null &&
                respuestaSeleccionada.equals(respuestaCorrecta);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

