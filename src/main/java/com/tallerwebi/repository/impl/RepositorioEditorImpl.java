package com.tallerwebi.repository.impl;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.model.Pregunta;
import com.tallerwebi.model.Respuesta;
import com.tallerwebi.repository.RepositorioEditor;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository("repositorioEditor")
@Transactional
public class RepositorioEditorImpl implements RepositorioEditor {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioEditorImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Pregunta> ObtenerTodasLasPreguntas() {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria(Pregunta.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                //.add(Restrictions.eq("habilitada", true))
                .createAlias("respuestas", "r"); // JOIN con respuestas

        return criteria.list();
    }

    @Override
    public List<Pregunta> ObtenerPreguntasPorCategoria(CATEGORIA_PREGUNTA categoria) {
        return List.of();
    }

    @Override
    public void ActualizarPregunta(String enunciado, List<Respuesta> Respuestas) {

    }
}
