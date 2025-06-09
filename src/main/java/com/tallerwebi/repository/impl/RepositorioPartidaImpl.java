package com.tallerwebi.repository.impl;

import com.tallerwebi.dominio.enums.ESTADO_PARTIDA;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.Partida;
import com.tallerwebi.model.RecoveryToken;
import com.tallerwebi.repository.RepositorioPartida;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository("repositorioPartida")
@Transactional
public class RepositorioPartidaImpl implements RepositorioPartida {
    private SessionFactory sessionFactory;

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
}
