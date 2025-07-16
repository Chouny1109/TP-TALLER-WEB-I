package com.tallerwebi.service;

import com.tallerwebi.model.Usuario;

import java.util.List;

public interface ServicioRanking {

    List<Usuario> obtenerTop10UsuariosPorXP();

    Integer obtenerPosicionDeUsuarioPorXP(Usuario usuario);

}
