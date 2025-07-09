package com.tallerwebi.repository.impl;

import com.tallerwebi.model.TrampaUsuario;
import com.tallerwebi.repository.RepositorioTrampaUsuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.List;

@Repository("repositorioTrampaUsuario")
public class RepositorioTrampaUsuarioImpl implements RepositorioTrampaUsuario {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioTrampaUsuarioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(TrampaUsuario trampaUsuario) {
        sessionFactory.getCurrentSession().save(trampaUsuario);
    }

    @Override
    public List<TrampaUsuario> obtenerTrampasDelUsuario(Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<TrampaUsuario> query = builder.createQuery(TrampaUsuario.class);
        Root<TrampaUsuario> root = query.from(TrampaUsuario.class);

        Join<?, ?> joinUsuario = root.join("usuario");
        query.select(root).where(builder.equal(joinUsuario.get("id"), idUsuario));

        return session.createQuery(query).getResultList();
    }

    @Override
    public TrampaUsuario buscarPorUsuarioYTrampa(Long idUsuario, Long idTrampa) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<TrampaUsuario> query = builder.createQuery(TrampaUsuario.class);
        Root<TrampaUsuario> root = query.from(TrampaUsuario.class);

        Predicate predUsuario = builder.equal(root.get("usuario").get("id"), idUsuario);
        Predicate predTrampa = builder.equal(root.get("trampa").get("id"), idTrampa);

        query.select(root).where(builder.and(predUsuario, predTrampa));

        return session.createQuery(query).uniqueResult();
    }

    @Override
    public void modificar(TrampaUsuario trampaUsuario) {
        sessionFactory.getCurrentSession().update(trampaUsuario);
    }
}
