package com.tallerwebi.repository;

import com.tallerwebi.model.Nivel;

public interface RepositorioNivel {
    Nivel obtenerNivelPorExperiencia(Integer experiencia);
}
