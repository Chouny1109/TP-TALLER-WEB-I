package com.tallerwebi.repository.impl;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositorioMisionUsuarioImpl implements RepositorioMisionUsuario {

    private final SessionFactory sessionFactory;

    public RepositorioMisionUsuarioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void borrarMisionesDelUsuario(Usuario usuario) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UsuarioMision.class)
                .add(Restrictions.eq("usuario", usuario));

        List<UsuarioMision> list = criteria.list();

        for (UsuarioMision usuarioMision : list) {
            sessionFactory.getCurrentSession().delete(usuarioMision);
        }
    }

    @Override
    public void saveAll(List<UsuarioMision> misiones) {
        for (UsuarioMision mision : misiones) {
            sessionFactory.getCurrentSession().save(mision);
        }
    }

    @Override
    public List<Mision> obtenerMisionesDelUsuarioPorId(Long id) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UsuarioMision.class)
                .createAlias("usuario", "u")
                .add(Restrictions.eq("u.id", id));
        List<UsuarioMision> list = criteria.list();
        List<Mision> misiones = new ArrayList<>();

        for (UsuarioMision relacion : list) {
            misiones.add(relacion.getMision());
        }
        return misiones;
    }
}
