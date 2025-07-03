package com.tallerwebi.repository.impl;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.model.Partida;
import com.tallerwebi.model.Pregunta;
import com.tallerwebi.model.Respuesta;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioPregunta;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Repository("repositorioPregunta")
@Transactional
public class RepositorioPreguntaImpl implements RepositorioPregunta {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioPreguntaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    /*@Override
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
    }*/





    @Override
    public Pregunta obtenerPregunta(CATEGORIA_PREGUNTA categoria, Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "select distinct p from Pregunta p " +
            "left join fetch p.respuestas r " +
            "where p.tipoPregunta = :categoria " +
            "and not exists (" +
            "   select 1 from UsuarioRespondePregunta urp " +
            "   where urp.pregunta = p and urp.usuario.id = :idUsuario" +
            ") " +
            "order by rand()";


        return (Pregunta) session.createQuery(hql)
            .setParameter("categoria", categoria)
            .setParameter("idUsuario", idUsuario)
            .setMaxResults(1)
            .uniqueResult();
    }


    @Override
    public List<Pregunta> listasPreguntasRandomParaPartida(Long idPregunta) {
        return List.of();
    }


    @Override
    public Pregunta buscarPreguntaPorId(Long idPregunta) {
        Session session = sessionFactory.getCurrentSession();

        return (Pregunta) session.createCriteria(Pregunta.class)
                .add(Restrictions.eq("id", idPregunta))
                .uniqueResult();

    }

    @Override
    public Respuesta buscarRespuestaPorId(Long idRespuesta) {
        Session session = sessionFactory.getCurrentSession();

        return (Respuesta) session.createCriteria(Respuesta.class)
                .add(Restrictions.eq("id", idRespuesta))
                .uniqueResult();

    }


    @Override
    @SuppressWarnings("unchecked")
    public Pregunta obtenerPreguntaSupervivencia(List<Usuario> jugadores) {
        Session session = sessionFactory.getCurrentSession();

        List<Long> idsJugadores = jugadores.stream()
                .map(Usuario::getId)
                .collect(Collectors.toList());

        String hql = "select distinct p from Pregunta p " +
                "where p.id not in (" +
                "   select urp.pregunta.id from UsuarioRespondePregunta urp " +
                "   where urp.usuario.id in (:idsJugadores)" +
                ") " +
                "order by function('rand')";

        List<Pregunta> preguntas = session.createQuery(hql)
                .setParameterList("idsJugadores", idsJugadores)
                .setMaxResults(1)
                .list();

        if (preguntas.isEmpty()) {
            return null;
        }

        return preguntas.get(0);
    }

    @Override
    public void actualizar(Pregunta pregunta) {
        Session session = sessionFactory.getCurrentSession();
        session.update(pregunta);
    }

    @Override
    public void actualizarRespuestas(Respuesta respuesta) {
        Session session = sessionFactory.getCurrentSession();
        session.update(respuesta);
    }

    @Override
    public void eliminarPregunta(Long idPregunta) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(this.buscarPreguntaPorId(idPregunta));
    }

    @Override
    public void agregarNuevaPregunta(Pregunta pregunta) {
        Session session = sessionFactory.getCurrentSession();
        session.save(pregunta);
    }

    @Override
    public List<Pregunta> obtenerPreguntasPorCategoria(String categoria) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Pregunta p WHERE p.tipoPregunta = :cat";
        return session.createQuery(hql, Pregunta.class)
                .setParameter("cat", CATEGORIA_PREGUNTA.valueOf(categoria))
                .getResultList();
    }
}
