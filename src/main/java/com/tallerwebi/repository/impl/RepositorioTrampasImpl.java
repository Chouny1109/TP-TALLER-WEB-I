package com.tallerwebi.repository.impl;

import com.tallerwebi.model.Trampa;
import com.tallerwebi.repository.RepositorioTrampas;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class RepositorioTrampasImpl implements RepositorioTrampas {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Trampa> buscarTrampasPorUsuario(Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<Trampa> query = builder.createQuery(Trampa.class);
        Root<Trampa> root = query.from(Trampa.class);

        // Filtro por id del usuario (asumiendo que hay una relación Trampa -> Usuario)
        query.select(root)
                .where(builder.equal(root.get("usuario").get("id"), idUsuario));

        return session.createQuery(query).getResultList();
    }
}


