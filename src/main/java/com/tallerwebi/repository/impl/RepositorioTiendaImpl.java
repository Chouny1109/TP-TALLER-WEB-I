package com.tallerwebi.repository.impl;

import com.tallerwebi.model.*;
import com.tallerwebi.repository.RepositorioTienda;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository("repositorioTienda")
public class RepositorioTiendaImpl implements RepositorioTienda{
    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioTiendaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Trampa> obtenerTrampas() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Trampa> query = builder.createQuery(Trampa.class);
        query.from(Trampa.class);
        return session.createQuery(query).getResultList();
    }

    @Override
    public List<Vida> obtenerVidas() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Vida> query = builder.createQuery(Vida.class);
        query.from(Vida.class);
        return session.createQuery(query).getResultList();
    }

    @Override
    public List<Moneda> obtenerMonedas() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Moneda> query = builder.createQuery(Moneda.class);
        query.from(Moneda.class);
        return session.createQuery(query).getResultList();
    }

    @Override
    public List<Avatar> obtenerAvatares() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Avatar> query = builder.createQuery(Avatar.class);
        query.from(Avatar.class);
        return session.createQuery(query).getResultList();
    }

    @Override
    public Moneda obtenerMonedaPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Moneda.class, id);
    }
}
