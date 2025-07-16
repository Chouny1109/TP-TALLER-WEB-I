package com.tallerwebi.service.impl;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Nivel;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioNivel;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioNivel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ServicioNivelImpl implements ServicioNivel {

    private final RepositorioNivel repositorioNivel;
    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioNivelImpl(RepositorioNivel repositorioNivel, RepositorioUsuario repositorioUsuario) {
        this.repositorioNivel = repositorioNivel;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Transactional
    @Override
    public Nivel obtenerNivelPorExperiencia(Integer experiencia) {
        return this.repositorioNivel.obtenerNivelPorExperiencia(experiencia);
    }

    @Override
    public Integer obtenerExperienciaRestanteParaElSiguienteNivel(Integer experiencia) {
        return 0;
    }

    @Transactional
    @Override
    public void verificarSiSubeDeNivel(Usuario usuario) {
        Integer experienciaActual = Optional.ofNullable(usuario.getExperiencia()).orElse(0);
        Integer nivelActual = usuario.getNivel();

        Nivel nuevoNivel = obtenerNivelPorExperiencia(experienciaActual);

        if (nuevoNivel.getNivel() > nivelActual) {
            usuario.setNivel(nuevoNivel.getNivel());
            this.repositorioUsuario.modificar(usuario);
        }
    }

    @Transactional
    @Override
    public void otorgarExperiencia(Usuario usuario, Mision mision) {
        if (usuario == null || mision == null) {
            throw new IllegalArgumentException("El usuario o la mision no pueden ser nulos");
        }
        Integer expActual = Optional.ofNullable(usuario.getExperiencia()).orElse(0);
        Integer expGanada = Optional.ofNullable(mision.getExperiencia()).orElse(0);
        usuario.setExperiencia(expActual + expGanada);

        verificarSiSubeDeNivel(usuario);

        this.repositorioUsuario.modificar(usuario);
    }
}
