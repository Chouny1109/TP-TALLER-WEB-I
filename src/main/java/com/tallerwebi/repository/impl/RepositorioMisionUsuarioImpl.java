package com.tallerwebi.repository.impl;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDate;
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
    public List<UsuarioMision> obtenerMisionesDelUsuarioPorId(Long id, LocalDate fecha) {

        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<UsuarioMision> query = builder.createQuery(UsuarioMision.class);
        Root<UsuarioMision> root = query.from(UsuarioMision.class);

        query.select(root)
                .where(builder.and(
                        builder.equal(root.get("usuario").get("id"), id),
                        builder.equal(root.get("fechaDeAsignacion"), fecha)
                ));
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

    @Override
    public UsuarioMision obtenerUsuarioMision(Long idMision) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<UsuarioMision> query = builder.createQuery(UsuarioMision.class);
        Root<UsuarioMision> root = query.from(UsuarioMision.class);

        query.select(root).where(builder.equal(root.get("id"), idMision));

        return sessionFactory.getCurrentSession().createQuery(query).getSingleResult();
    }

    @Override
    public void delete(UsuarioMision usuarioMision) {
        sessionFactory.getCurrentSession().delete(usuarioMision);
    }

    @Override
    public Mision obtenerMisionNoAsignadaParaUsuario(Long usuarioId, LocalDate fecha) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Mision> query = builder.createQuery(Mision.class);
        Root<Mision> rootMision = query.from(Mision.class);
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<UsuarioMision> subRoot = subquery.from(UsuarioMision.class);

        subquery.select(builder.literal(1L))
                .where(
                        builder.equal(subRoot.get("mision"), rootMision),
                        builder.equal(subRoot.get("usuario").get("id"), usuarioId),
                        builder.equal(subRoot.get("fechaDeAsignacion"), fecha)
                );

        query.select(rootMision)
                .where(builder.not(builder.exists(subquery)));

        return session.createQuery(query)
                .setMaxResults(1)
                .uniqueResult();
    }
}
