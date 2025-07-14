package com.tallerwebi.repository.impl;

import com.tallerwebi.model.Avatar;
import com.tallerwebi.model.Habilidad;
import com.tallerwebi.model.Usuario;
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
    public void cambiarAvatar(Long idAvatar, Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery("UPDATE Usuario u SET avatarActual_id = :idAvatar WHERE u.id = :idUsuario")
                .setParameter("idUsuario", idUsuario)
                .setParameter("idAvatar", idAvatar)
                .executeUpdate();
    }

    @Override
    public Habilidad obtenerHabilidadAvatar() {
        return null;
    }

    @Override
    public List<Avatar> obtenerAvataresNoDisponibles(Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "SELECT a " +
                "FROM Avatar a " +
                "WHERE a.id NOT IN (" +
                "   SELECT a2.id " +
                "   FROM Usuario u JOIN u.avataresEnPropiedad a2 " +
                "   WHERE u.id = :idUsuario" +
                ")";

        return session.createQuery(hql, Avatar.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    @Override
    public List<Avatar> obtenerAvataresDisponibles(Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "SELECT a FROM Usuario u JOIN u.avataresEnPropiedad a WHERE u.id = :idUsuario";

        return session.createQuery(hql, Avatar.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    @Override
    public void guardarAvatarAUsuario(Long idAvatar, Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery("INSERT INTO UsuarioAvatar (usuario_id, avatar_id) VALUES (:uid, :aid)")
                .setParameter("uid", idUsuario)
                .setParameter("aid", idAvatar)
                .executeUpdate();
    }

    @Override
    public void actualizar(Usuario usuario) {
        Session session = sessionFactory.getCurrentSession();
        session.update(usuario);
    }
}
