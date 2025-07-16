package com.tallerwebi.service;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Nivel;
import com.tallerwebi.model.Usuario;

public interface ServicioNivel {

    Nivel obtenerNivelPorExperiencia(Integer experiencia);

    Integer obtenerExperienciaRestanteParaElSiguienteNivel(Integer experiencia);

    void verificarSiSubeDeNivel(Usuario usuario);

    void otorgarExperiencia(Usuario usuario, Mision mision);
}
