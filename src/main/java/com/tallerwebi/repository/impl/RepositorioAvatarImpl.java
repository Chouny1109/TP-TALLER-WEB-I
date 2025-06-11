package com.tallerwebi.repository.impl;

import com.tallerwebi.model.Avatar;
import com.tallerwebi.repository.RepositorioAvatar;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("repositorioAvatar")
@Transactional
public class RepositorioAvatarImpl implements RepositorioAvatar {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioAvatarImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Avatar obtenerAvatar(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return (Avatar)session.createCriteria(Avatar.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }
}
