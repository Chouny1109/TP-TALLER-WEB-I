package com.tallerwebi.service.impl;

import com.tallerwebi.model.Avatar;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioAvatar;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioAvatar;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("ServicioAvatar")
@Transactional
public class ServicioAvatarImpl implements ServicioAvatar {


    private RepositorioAvatar repositorioAvatar;
    private RepositorioUsuario repositorioUsuario;
    private SessionFactory sessionFactory;

    @Autowired
    public ServicioAvatarImpl(RepositorioAvatar repositorioAvatar, RepositorioUsuario repositorioUsuario, SessionFactory sessionFactory) {
        this.repositorioAvatar = repositorioAvatar;
        this.repositorioUsuario = repositorioUsuario;
        this.sessionFactory = sessionFactory;
    }
    @Override
    public void guardarAvatarAUsuario(Long idAvatar, Long idUsuario) {
        repositorioAvatar.guardarAvatarAUsuario(idAvatar, idUsuario);
    }

    @Override
    public Avatar buscarPorId(Long idAvatar) {
        return repositorioAvatar.obtenerAvatar(idAvatar);
    }

    @Override
    public void cambiarAvatar(Long idAvatar, Long idUsuario) {
        repositorioAvatar.cambiarAvatar(idAvatar, idUsuario);

    }

    @Override
    public List<Avatar> obtenerAvataresDisponibles(Long idUsuario) {
        return repositorioAvatar.obtenerAvataresDisponibles(idUsuario);
    }

    @Override
    public List<Avatar> obtenerAvataresNoDisponibles(Long idUsuario) {
        return repositorioAvatar.obtenerAvataresNoDisponibles(idUsuario);
    }

    @Transactional
    @Override
    public void comprarAvatar(Long idAvatar, Long idUsuario) {
        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(idUsuario);
        Avatar avatar = repositorioAvatar.obtenerAvatar(idAvatar);

        if (avatar != null && usuario.getMonedas() >= avatar.getValor()) {
            if (!usuario.getAvataresEnPropiedad().contains(avatar)) {
                usuario.getAvataresEnPropiedad().add(avatar);
                usuario.setMonedas(usuario.getMonedas() - avatar.getValor());

                this.actualizar(usuario);
            }
        }
    }



    @Override
    public void actualizar(Usuario usuario) {
        repositorioAvatar.actualizar(usuario);
    }

    @Override
    public Usuario buscarUsuarioPorId(Long id) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Usuario WHERE id = :id", Usuario.class)
                .setParameter("id", id)
                .uniqueResult();
    }
}
