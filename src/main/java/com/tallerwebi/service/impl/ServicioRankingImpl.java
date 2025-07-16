package com.tallerwebi.service.impl;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioRanking;
import com.tallerwebi.service.ServicioRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioRankingImpl implements ServicioRanking {


    RepositorioRanking repositorioRanking;

    @Autowired
    public ServicioRankingImpl(RepositorioRanking repositorioRanking) {
        this.repositorioRanking = repositorioRanking;
    }

    @Override
    public List<Usuario> obtenerTop10UsuariosPorXP() {
        return repositorioRanking.obtenerTop10UsuariosPorXP();
    }

    @Override
    public Integer obtenerPosicionDeUsuarioPorXP(Usuario usuario) {
        return repositorioRanking.obtenerPosicionDeUsuarioPorXP(usuario);
    }
}
