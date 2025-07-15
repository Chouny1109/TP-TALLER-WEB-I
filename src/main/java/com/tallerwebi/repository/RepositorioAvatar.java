package com.tallerwebi.repository;

import com.tallerwebi.model.Avatar;
import com.tallerwebi.model.Habilidad;
import com.tallerwebi.model.Usuario;

import java.util.List;

public interface RepositorioAvatar {

    Avatar obtenerAvatar(Long id);
    void cambiarAvatar(Long idAvatar, Long idUsuario);
    Habilidad obtenerHabilidadAvatar();
    List<Avatar> obtenerAvataresDisponibles(Long idUsuario);
    List<Avatar> obtenerAvataresNoDisponibles(Long idUsuario);
    void guardarAvatarAUsuario(Long idAvatar, Long idUsuario);
    void actualizar(Usuario usuario);
}
