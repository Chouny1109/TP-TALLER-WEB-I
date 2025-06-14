package com.tallerwebi.repository;

import com.tallerwebi.model.Avatar;
import com.tallerwebi.model.Habilidad;

import java.util.List;

public interface RepositorioAvatar {

    Avatar obtenerAvatar(Long id);
    void cambiarAvatar(Long idAvatar, Long idUsuario, int nuevoEstado);
    Habilidad obtenerHabilidadAvatar();
    List<Avatar> obtenerAvataresDisponibles(Long idUsuario);
    List<Avatar> obtenerAvataresNoDisponibles(Long idUsuario);
}
