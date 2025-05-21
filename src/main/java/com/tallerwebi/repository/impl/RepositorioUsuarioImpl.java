package com.tallerwebi.repository.impl;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioUsuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("repositorioUsuario")
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Usuario buscarUsuario(String email, String password) {
        final Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
        Root<Usuario> root = query.from(Usuario.class);

        query.select(root)
                .where(
                        builder.and(
                                builder.equal(root.get("email"), email),
                                builder.equal(root.get("password"), password)
                        )
                );

        return session.createQuery(query).uniqueResult();
    }

    @Override
    public Boolean guardar(Usuario usuario) {
        sessionFactory.getCurrentSession().save(usuario);
        return true;
    }

    @Override
    public Usuario buscar(String email) {
        Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
        Root<Usuario> root = query.from(Usuario.class);

        query.select(root)
                .where(builder.equal(root.get("email"), email));

        return session.createQuery(query).uniqueResult();
    }


    @Override
    public void modificar(Usuario usuario) {
        sessionFactory.getCurrentSession().update(usuario);
    }

    @Override
    public Usuario buscarUsuarioPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
        Root<Usuario> root = query.from(Usuario.class);

        query.select(root)
                .where(builder.equal(root.get("id"), id));

        return session.createQuery(query).uniqueResult();
    }

    @Override
    public List<Mision> obtenerMisiones(Long id) {
        Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Mision> query = builder.createQuery(Mision.class);
        Root<Mision> root = query.from(Mision.class);

        query.select(root)
                .where(builder.equal(root.get("usuario").get("id"), id));
        return session.createQuery(query).getResultList();
    }

}
