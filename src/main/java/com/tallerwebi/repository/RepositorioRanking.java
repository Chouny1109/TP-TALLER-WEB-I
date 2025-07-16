package com.tallerwebi.repository;

import com.tallerwebi.model.Usuario;

import java.util.List;

public interface RepositorioRanking {
    List<Usuario> obtenerTop10UsuariosPorXP();

    Integer obtenerPosicionDeUsuarioPorXP(Usuario usuario);


}
