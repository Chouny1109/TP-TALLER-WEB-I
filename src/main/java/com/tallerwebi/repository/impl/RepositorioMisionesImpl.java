package com.tallerwebi.repository.impl;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioMisiones;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class RepositorioMisionesImpl implements RepositorioMisiones {

    private final SessionFactory sessionFactory;

    public RepositorioMisionesImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Mision> obtenerMisiones() {
        CriteriaBuilder builder = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Mision> criteriaQuery = builder.createQuery(Mision.class);
        Root<Mision> root = criteriaQuery.from(Mision.class);

        criteriaQuery.select(root).distinct(true);

        return sessionFactory.getCurrentSession().createQuery(criteriaQuery).list();

    }
}
