package com.tallerwebi.repository.impl;

import com.tallerwebi.model.Avatar;
import com.tallerwebi.model.Habilidad;
import com.tallerwebi.repository.RepositorioAvatar;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("repositorioAvatar")
@Transactional
public class RepositorioAvatarImpl implements RepositorioAvatar {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioAvatarImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Avatar obtenerAvatar(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return (Avatar)session.createCriteria(Avatar.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public void cambiarAvatar(Long idAvatar, Long idUsuario, int nuevoEstado) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery("UPDATE UsuarioAvatar SET estadoAvatar = :estado, avatar_id = :idAvatar WHERE usuario_id = :idUsuario")
                .setParameter("estado", nuevoEstado)
                .setParameter("idUsuario", idUsuario)
                .setParameter("idAvatar", idAvatar)
                .executeUpdate();
    }

    @Override
    public Habilidad obtenerHabilidadAvatar() {
        return null;
    }

    @Override
    public List<Avatar> obtenerAvataresDisponibles(Long idUsuario) {
        return List.of();
    }

    @Override
    public List<Avatar> obtenerAvataresNoDisponibles(Long idUsuario) {
        return List.of();
    }
}
