package com.tallerwebi.repository.impl;

import com.tallerwebi.dominio.enums.ESTADO_PARTIDA;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.Partida;
import com.tallerwebi.model.RecoveryToken;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioPartida;
import com.tallerwebi.repository.RepositorioPartida;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Repository("repositorioPartida")
@Transactional
public class RepositorioPartidaImpl implements RepositorioPartida {
    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioPartidaImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Boolean guardarPartida(Partida partida) {
       sessionFactory.getCurrentSession().save(partida);
       return true;
    }

    @Override
    public void actualizarPartida(Partida partida) {
        sessionFactory.getCurrentSession().update(partida);

    }

    @Override
    public Partida obtenerPartidaAbiertaPorModo(TIPO_PARTIDA tipo){
        Session session = sessionFactory.getCurrentSession();
        return (Partida) session.createCriteria(Partida.class)
                .add(Restrictions.eq("estadoPartida", ESTADO_PARTIDA.ABIERTA))
                .add(Restrictions.eq("tipo", tipo))
                .uniqueResult();
    }

    @Override
    public void agregarUsuarioPartidaRelacion(UsuarioPartida usuarioPartida) {
        sessionFactory.getCurrentSession().save(usuarioPartida);
    }
    @Override
    public Usuario obtenerRivalDePartida(Long idPartida, Long idJugadorActual) {
        Session session = sessionFactory.getCurrentSession();

        UsuarioPartida usuarioPartida = (UsuarioPartida) session.createCriteria(UsuarioPartida.class)
                .createAlias("partida", "p")
                .createAlias("usuario", "u")
                .add(Restrictions.eq("p.id", idPartida))
                .add(Restrictions.ne("u.id", idJugadorActual))  // distinto al jugador actual
                .setMaxResults(1)
                .uniqueResult();

        return usuarioPartida != null ? usuarioPartida.getUsuario() : null;
    }

    @Override
    public List<Usuario> obtenerJugadoresDePartida(Long idPartida) {
        Session session = sessionFactory.getCurrentSession();

        List<UsuarioPartida> listaUP = session.createCriteria(UsuarioPartida.class)
                .createAlias("partida", "p")
                .createAlias("usuario", "u")
                .add(Restrictions.eq("p.id", idPartida))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();

        return listaUP.stream()
                .map(UsuarioPartida::getUsuario)
                .collect(Collectors.toList());
    }


}
