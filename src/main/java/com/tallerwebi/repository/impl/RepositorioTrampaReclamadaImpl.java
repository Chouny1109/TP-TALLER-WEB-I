package com.tallerwebi.repository.impl;

import com.tallerwebi.model.Trampa;
import com.tallerwebi.model.TrampaReclamada;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioTrampaReclamada;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;

@Repository("repositorioTrampaReclamada")
public class RepositorioTrampaReclamadaImpl implements RepositorioTrampaReclamada {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioTrampaReclamadaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean yaReclamo(Usuario usuario, Trampa trampa, LocalDate fecha) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<TrampaReclamada> query = builder.createQuery(TrampaReclamada.class);
        Root<TrampaReclamada> root = query.from(TrampaReclamada.class);

        Predicate predUsuario = builder.equal(root.get("usuario"), usuario);
        Predicate predTrampa = builder.equal(root.get("trampa"), trampa);
        Predicate predFecha = builder.equal(root.get("fecha"), fecha);

        query.select(root).where(builder.and(predUsuario, predTrampa, predFecha));

        return session.createQuery(query).uniqueResult() != null;
    }


    @Override
    public void guardar(TrampaReclamada reclamacion) {
        sessionFactory.getCurrentSession().save(reclamacion);
    }
}
