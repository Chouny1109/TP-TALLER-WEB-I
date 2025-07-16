package com.tallerwebi.repository.impl;

import com.tallerwebi.model.Nivel;
import com.tallerwebi.repository.RepositorioNivel;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class RepositorioNivelImpl implements RepositorioNivel {

    private final SessionFactory sessionFactory;

    public RepositorioNivelImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Nivel obtenerNivelPorExperiencia(Integer experiencia) {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Nivel> query = cb.createQuery(Nivel.class);
        Root<Nivel> root = query.from(Nivel.class);

        query.select(root)
                .where(cb.lessThanOrEqualTo(root.get("experienciaNecesaria"), experiencia))
                .orderBy(cb.desc(root.get("nivel"))); // Trae el más alto posible

        return sessionFactory.getCurrentSession().createQuery(query)
                .setMaxResults(1)
                .getSingleResult();
    }
}
