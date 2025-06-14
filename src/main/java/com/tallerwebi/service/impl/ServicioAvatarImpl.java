package com.tallerwebi.service.impl;

import com.tallerwebi.model.Avatar;
import com.tallerwebi.repository.RepositorioAvatar;
import com.tallerwebi.service.ServicioAvatar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("ServicioAvatar")
@Transactional
public class ServicioAvatarImpl implements ServicioAvatar {

    private RepositorioAvatar repositorioAvatar;

    @Autowired
    public void ServicioAvatarImpl(RepositorioAvatar repositorioAvatar){this.repositorioAvatar = repositorioAvatar;}

    @Override
    public Boolean cambiarAvatar(Long idAvatar, Long idUsuario) {
        int nuevoEstado = 0;
        repositorioAvatar.cambiarAvatar(idAvatar, idUsuario, nuevoEstado);
        return null;
    }

    @Override
    public List<Avatar> obtenerAvataresDisponibles(Long idUsuario) {
        return repositorioAvatar.obtenerAvataresDisponibles(idUsuario);
    }

    @Override
    public List<Avatar> obtenerAvataresNoDisponibles(Long idUsuario) {
        return repositorioAvatar.obtenerAvataresNoDisponibles(idUsuario);
    }
}
