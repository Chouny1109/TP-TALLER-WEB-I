package com.tallerwebi.repository.impl;

import com.tallerwebi.dominio.enums.ESTADO_AVATAR;
import com.tallerwebi.model.*;
import com.tallerwebi.repository.RepositorioUsuario;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hsqldb.rights.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("repositorioUsuario")
@Transactional
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Boolean guardar(Usuario usuario) {
        sessionFactory.getCurrentSession().save(usuario);
        return true;
    }

    @Override
    public void asignarAvatarPorDefecto(UsuarioAvatar ua) {
        sessionFactory.getCurrentSession().save(ua);

    }


    @Override
    public Usuario buscar(String email) {
        Session session = sessionFactory.getCurrentSession();

        return (Usuario) session.createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .uniqueResult();

    }

    @Override
    public void modificar(Usuario usuario) {
        sessionFactory.getCurrentSession().update(usuario);
    }

    @Override
    public Usuario buscarUsuarioPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
        Root<Usuario> root = query.from(Usuario.class);

        query.select(root)
                .where(builder.equal(root.get("id"), id));

        return session.createQuery(query).uniqueResult();
    }

    @Override
    public String obtenerImagenAvatarSeleccionado(Long id){
        Session session = sessionFactory.getCurrentSession();

        UsuarioAvatar ua = (UsuarioAvatar) session.createCriteria(UsuarioAvatar.class)
                .add(Restrictions.eq("usuario.id", id))
                .add(Restrictions.eq("estado", ESTADO_AVATAR.SELECCIONADO))
                .uniqueResult();

        if (ua != null && ua.getAvatar() != null) {
            return ua.getAvatar().getLink();
        } else {
            return null;
        }
    }

    @Override
    public Usuario obtenerUsuarioConPartidas(Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria(Usuario.class)
                .add(Restrictions.eq("id", idUsuario))
                .setFetchMode("partidas", FetchMode.JOIN)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return (Usuario) criteria.uniqueResult();
    }


}
