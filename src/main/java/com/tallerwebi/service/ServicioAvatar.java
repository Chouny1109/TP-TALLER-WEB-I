package com.tallerwebi.service;


import com.tallerwebi.model.Avatar;

import java.util.List;

public interface ServicioAvatar {
    Boolean cambiarAvatar(Long idAvatar, Long idUsuario);
    List<Avatar> obtenerAvataresDisponibles(Long idUsuario);
    List<Avatar> obtenerAvataresNoDisponibles(Long idUsuario);
}
