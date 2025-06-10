package com.tallerwebi.repository.impl;

import com.tallerwebi.model.Amigo;
import com.tallerwebi.repository.RepositorioAmigo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository("repositorioAmigo")
public class RepositorioAmigoImpl implements RepositorioAmigo {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioAmigoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Amigo> obtenerAmigos() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Amigo> query = builder.createQuery(Amigo.class);
        query.from(Amigo.class);
        return session.createQuery(query).getResultList();
    }
}
