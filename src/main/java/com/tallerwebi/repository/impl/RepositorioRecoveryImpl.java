package com.tallerwebi.repository.impl;

import com.tallerwebi.model.RecoveryToken;
import com.tallerwebi.repository.RepositorioRecovery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository("repositorioRecovery")
@Transactional
public class RepositorioRecoveryImpl implements RepositorioRecovery {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioRecoveryImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Boolean guardar(RecoveryToken token) {
        sessionFactory.getCurrentSession().save(token);
        return true;
    }

    @Override
    public RecoveryToken buscarToken(String token) {
        Session session = sessionFactory.getCurrentSession();
        return (RecoveryToken) session.createCriteria(RecoveryToken.class)
                .add(Restrictions.eq("token", token))
                .uniqueResult();
    }

    @Override
    public void actualizar(RecoveryToken token) {
        sessionFactory.getCurrentSession().update(token);
    }
}
