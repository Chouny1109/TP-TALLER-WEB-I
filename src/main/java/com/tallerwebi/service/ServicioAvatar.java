package com.tallerwebi.service;


import com.tallerwebi.model.Avatar;
import com.tallerwebi.model.Usuario;

import javax.transaction.Transactional;
import java.util.List;

public interface ServicioAvatar {
    void guardarAvatarAUsuario(Long idAvatar, Long idUsuario);
    Avatar buscarPorId(Long idAvatar);
    void cambiarAvatar(Long idAvatar, Long idUsuario);
    List<Avatar> obtenerAvataresDisponibles(Long idUsuario);
    List<Avatar> obtenerAvataresNoDisponibles(Long idUsuario);

    @Transactional
    void comprarAvatar(Long idAvatar, Long idUsuario);

    void actualizar(Usuario usuario);

    Usuario buscarUsuarioPorId(Long id);
}
