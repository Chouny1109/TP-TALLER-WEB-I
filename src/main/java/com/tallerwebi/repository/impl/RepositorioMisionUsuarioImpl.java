package com.tallerwebi.repository.impl;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Mision> query = builder.createQuery(Mision.class);
        Root<UsuarioMision> root = query.from(UsuarioMision.class);

        query.select(root.get("mision"));//obtenemos el atributo mision de UsuarioMision

        query.where(builder.equal(root.get("usuario").get("id"), id));

        return sessionFactory.getCurrentSession().createQuery(query).getResultList();
    }

    @Override
    public Set<Long> obtenerElIdDeTodosLosUsuariosConMisionesAsignadas(LocalDate fechaActual) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder(); // Permite hacer consultas
        CriteriaQuery<Long> query = builder.createQuery(Long.class);//Que quiero obtener
        Root<UsuarioMision> root = query.from(UsuarioMision.class); // de donde saco la informacion

        query.select(root.get("usuario").get("id")) // seleccioname el id de todos los usuarios donde
                // la fecha actual sea igual a la fecha por parametro
                .where(builder.equal(root.get("fechaDeAsignacion"), fechaActual))
                .distinct(true);//para no repetir ids

        List<Long> ids = sessionFactory.getCurrentSession().createQuery(query).getResultList();

        return new HashSet<>(ids);
    }

    @Override
    public void save(UsuarioMision usuarioMision) {
        sessionFactory.getCurrentSession().save(usuarioMision);
    }


}
