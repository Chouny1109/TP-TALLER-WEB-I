package com.tallerwebi.repository.impl;

import com.tallerwebi.model.Mision;
import com.tallerwebi.repository.RepositorioMisiones;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioMisionesImpl implements RepositorioMisiones {

    private final SessionFactory sessionFactory;

    public RepositorioMisionesImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public List<Mision> obtenerMisiones() {
        Session session = sessionFactory.getCurrentSession();
        return (List<Mision>)session.createCriteria(Mision.class).list();
    }
}
