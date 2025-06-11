package com.tallerwebi.model;

import com.tallerwebi.dominio.enums.TIPO_PARTIDA;

public class PartidaRequest {

    private Long usuarioId;
    private TIPO_PARTIDA modoJuego;

    public PartidaRequest() {
    }

    public PartidaRequest(Long usuarioId, TIPO_PARTIDA modoJuego) {
        this.usuarioId = usuarioId;
        this.modoJuego = modoJuego;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public TIPO_PARTIDA getModoJuego() {
        return modoJuego;
    }

    public void setModoJuego(TIPO_PARTIDA modoJuego) {
        this.modoJuego = modoJuego;
    }
}
