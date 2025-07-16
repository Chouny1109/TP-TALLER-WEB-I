package com.tallerwebi.repository;

import com.tallerwebi.model.Nivel;
import com.tallerwebi.model.NivelUsuarioDTO;
import com.tallerwebi.model.Usuario;

public interface RepositorioNivel {
    Nivel obtenerNivelPorExperiencia(Integer experiencia);

    Nivel obtenerNivelSiguiente(Integer nivel);

}
