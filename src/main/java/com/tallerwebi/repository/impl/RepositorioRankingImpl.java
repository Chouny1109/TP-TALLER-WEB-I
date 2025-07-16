package com.tallerwebi.repository.impl;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioRanking;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository("repositorioRanking")
@Transactional
public class RepositorioRankingImpl implements RepositorioRanking {


    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioRankingImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Usuario> obtenerTop10UsuariosPorXP() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Usuario u ORDER BY u.experiencia DESC";
        return session.createQuery(hql, Usuario.class)
                .setMaxResults(10)
                .getResultList();
    }
    @Override
    public Integer obtenerPosicionDeUsuarioPorXP(Usuario usuario) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(u) FROM Usuario u " +
                "WHERE (u.experiencia > :xp) " +
                "OR (u.experiencia = :xp AND u.id < :id)";

        Long count = session.createQuery(hql, Long.class)
                .setParameter("xp", usuario.getExperiencia())
                .setParameter("id", usuario.getId())
                .getSingleResult();

        return count.intValue() + 1;
    }


}
