package com.tallerwebi.service.impl;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Nivel;
import com.tallerwebi.model.NivelUsuarioDTO;
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

    @Transactional
    @Override
    public Nivel obtenerNivelSiguiente(Integer nivel) {
        return this.repositorioNivel.obtenerNivelSiguiente(nivel);
    }

    @Override
    @Transactional
    public NivelUsuarioDTO construirInfoDeNivel(Usuario usuario) {
        Integer nivelActual = usuario.getNivel();
        Integer experienciaActual = usuario.getExperiencia();

        Nivel nivelActualObj = obtenerNivelPorExperiencia(usuario.getExperiencia());
        Nivel siguiente = obtenerNivelSiguiente(nivelActual);

        if (siguiente == null) {
            return new NivelUsuarioDTO(nivelActual, null, experienciaActual, null, null, 100);
        }

        Integer experienciaBase = nivelActualObj.getExperienciaNecesaria();
        Integer experienciaNecesaria = siguiente.getExperienciaNecesaria();
        Integer restante = Math.max(0, experienciaNecesaria - experienciaActual);

        int totalNecesaria = experienciaNecesaria - experienciaBase;
        int experienciaGanada = experienciaActual - experienciaBase;
        int porcentaje = (int) ((double) experienciaGanada * 100 / totalNecesaria);

        return new NivelUsuarioDTO(
                nivelActual,
                siguiente.getNivel(),
                experienciaActual,
                experienciaNecesaria,
                restante,
                porcentaje
        );
    }


    @Transactional
    @Override
    public void verificarSiSubeDeNivel(Usuario usuario) {
        Integer experienciaActual = Optional.ofNullable(usuario.getExperiencia()).orElse(0);//200
        Integer nivelActual = usuario.getNivel();//1

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
