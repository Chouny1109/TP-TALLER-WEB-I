package com.tallerwebi.strategys.Mision;

import com.tallerwebi.dominio.enums.TIPO_MISION;

import java.util.HashMap;
import java.util.Map;

public class MisionFactory {

    private static final Map<TIPO_MISION, EstrategiaMision> estrategias = new HashMap<>() {{
        put(TIPO_MISION.GANAR_PARTIDAS, new MisionGanarPartidas());
        put(TIPO_MISION.JUGAR_PARTIDAS, new MisionJugarPartidas());
        put(TIPO_MISION.USAR_HABILIDADES, new MisionUsarHabilidad());
        put(TIPO_MISION.INICIAR_SESION, new MisionIniciarSesion());
        put(TIPO_MISION.NO_USAR_HABILIDADES, new MisionNoUsarHabilidades());
        put(TIPO_MISION.CONSULTAR_TABLA_POSICION, new MisionConsultarTablas());
        put(TIPO_MISION.GANAR_PARTIDA_CONSECUTIVA, new MisionGanarPartidasConsecutivas());
    }};

    public EstrategiaMision obtenerEstrategia(TIPO_MISION tipo) {
        return estrategias.get(tipo);
    }
}
