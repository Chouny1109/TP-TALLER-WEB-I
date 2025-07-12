package com.tallerwebi.service;

import com.tallerwebi.model.Nivel;

public interface ServicioNivel {

    Nivel obtenerNivelPorExperiencia(Integer experiencia);

    Integer obtenerExperienciaRestanteParaElSiguienteNivel(Integer experiencia);

}
