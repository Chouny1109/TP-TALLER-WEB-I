package com.tallerwebi.strategys.Mision;

import com.tallerwebi.dominio.enums.TIPO_MISION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MisionFactory {

    private final Map<TIPO_MISION, EstrategiaMision> estrategias = new HashMap<>();

    public MisionFactory(MisionGanarPartidas ganarPartidas,
                         MisionJugarPartidas jugarPartidas,
                         MisionUsarHabilidad usarHabilidades,
                         MisionIniciarSesion iniciarSesion,
                         MisionNoUsarHabilidades noUsarHabilidades,
                         MisionConsultarTablas consultarTablas,
                         MisionGanarPartidasConsecutivas ganarPartidasConsecutivas) {

        estrategias.put(TIPO_MISION.GANAR_PARTIDAS, ganarPartidas);
        estrategias.put(TIPO_MISION.JUGAR_PARTIDAS, jugarPartidas);
        estrategias.put(TIPO_MISION.USAR_HABILIDADES, usarHabilidades);
        estrategias.put(TIPO_MISION.INICIAR_SESION, iniciarSesion);
        estrategias.put(TIPO_MISION.NO_USAR_HABILIDADES, noUsarHabilidades);
        estrategias.put(TIPO_MISION.CONSULTAR_TABLA_POSICION, consultarTablas);
        estrategias.put(TIPO_MISION.GANAR_PARTIDA_CONSECUTIVA, ganarPartidasConsecutivas);
    }

    public EstrategiaMision obtenerEstrategia(TIPO_MISION tipo) {
        return estrategias.get(tipo);
    }
}
