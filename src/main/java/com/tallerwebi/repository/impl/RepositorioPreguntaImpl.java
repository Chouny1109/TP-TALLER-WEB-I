package com.tallerwebi.repository.impl;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.model.Pregunta;
import com.tallerwebi.repository.RepositorioPregunta;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.hibernate.criterion.Order;

import javax.transaction.Transactional;
import java.util.List;

@Repository("repositorioPregunta")
@Transactional
public class RepositorioPreguntaImpl implements RepositorioPregunta {

    private final SessionFactory sessionFactory;

@Autowired
    public RepositorioPreguntaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Pregunta obtenerPregunta(CATEGORIA_PREGUNTA categoria, Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria(Pregunta.class, "pregunta")
                .createAlias("pregunta.respuestasUsuarios", "resp", Criteria.LEFT_JOIN)
                .createAlias("pregunta.respuestas", "res", Criteria.LEFT_JOIN) // 👈 para hacer fetch de respuestas
                .add(Restrictions.eq("pregunta.tipoPregunta", categoria))
                .add(Restrictions.or(
                        Restrictions.isNull("resp.id"),
                        Restrictions.ne("resp.usuario.id", idUsuario)
                ))
                .add(Restrictions.sqlRestriction("1=1 order by rand()"))
                .setMaxResults(1)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return (Pregunta) criteria.uniqueResult();
    }


    @Override
    public Pregunta buscarPreguntaPorId() {
        return null;
    }

    @Override
    public List<Pregunta> listasPreguntasRandomParaPartida() {
        return List.of();
    }
}
