package com.tallerwebi.service.impl;

import com.tallerwebi.model.Nivel;
import com.tallerwebi.repository.RepositorioNivel;
import com.tallerwebi.service.ServicioNivel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioNivelImpl implements ServicioNivel {

    private final RepositorioNivel repositorioNivel;

    @Autowired
    public ServicioNivelImpl(RepositorioNivel repositorioNivel) {
        this.repositorioNivel = repositorioNivel;
    }

    @Override
    public Nivel obtenerNivelPorExperiencia(Integer experiencia) {
        return null;
    }

    @Override
    public Integer obtenerExperienciaRestanteParaElSiguienteNivel(Integer experiencia) {
        return 0;
    }
}
