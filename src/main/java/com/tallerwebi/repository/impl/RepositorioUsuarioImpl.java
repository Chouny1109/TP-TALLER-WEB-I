package com.tallerwebi.repository.impl;

import com.tallerwebi.dominio.enums.ESTADO_AVATAR;
import com.tallerwebi.dominio.enums.ROL_USUARIO;
import com.tallerwebi.model.*;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioUsuario;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("repositorioUsuario")
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Boolean guardar(Usuario usuario) {
        sessionFactory.getCurrentSession().save(usuario);
        return true;
    }

    @Override
    public void asignarAvatarPorDefecto(UsuarioAvatar ua) {
        sessionFactory.getCurrentSession().save(ua);

    }


    @Override
    public Usuario buscar(String email) {
        Session session = sessionFactory.getCurrentSession();

        return (Usuario) session.createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .uniqueResult();

    }

    @Override
    public void modificar(Usuario usuario) {
        sessionFactory.getCurrentSession().merge(usuario);
    }

    @Override
    public Usuario buscarUsuarioPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();

        return (Usuario) session.createCriteria(Usuario.class)
                .add(Restrictions.eq("id", id))
                .setFetchMode("amigos", FetchMode.JOIN)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .uniqueResult();
    }

    @Override
    public List<Usuario> obtenerUsuarios() {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
        Root<Usuario> root = query.from(Usuario.class);

        query.select(root).distinct(true);

        return sessionFactory.getCurrentSession().createQuery(query).getResultList();
    }

    @Override
    public String obtenerImagenAvatarSeleccionado(Long id) {
        Session session = sessionFactory.getCurrentSession();

        UsuarioAvatar ua = (UsuarioAvatar) session.createCriteria(UsuarioAvatar.class)
                .add(Restrictions.eq("usuario.id", id))
                .add(Restrictions.eq("estado", ESTADO_AVATAR.SELECCIONADO))
                .uniqueResult();

        if (ua != null && ua.getAvatar() != null) {
            return ua.getAvatar().getLink();
        } else {
            return null;
        }
    }

    @Override
    public Usuario obtenerUsuarioConPartidas(Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria(Usuario.class)
                .add(Restrictions.eq("id", idUsuario))
                .setFetchMode("partidas", FetchMode.JOIN)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return (Usuario) criteria.uniqueResult();
    }

    @Override
    public List<Usuario> obtenerAmigos(Long idUsuario) {
        Usuario usuario = buscarUsuarioPorId(idUsuario);
        if (usuario != null) {
            return List.copyOf(usuario.getAmigos());
        }
        return List.of();
    }

    @Override
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        Session session = sessionFactory.getCurrentSession();
        return (Usuario) session.createCriteria(Usuario.class)
                .add(Restrictions.eq("nombreUsuario", nombreUsuario))
                .uniqueResult();
    }

    @Override
    public Long contarUsuarios() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(u) FROM Usuario u";
        Long cantidad = (Long) session.createQuery(hql).uniqueResult();
        return cantidad;
    }

    @Override
    public void banearUsuario(Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();

            String hql = "update Usuario set baneado = 1 where id = :id";
            session.createQuery(hql)
                    .setParameter("id", idUsuario)
                    .executeUpdate();

    }

    @Override
    public void desbanearUsuario(Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();

            String hql = "update Usuario set baneado = 0 where id = :id";
            session.createQuery(hql)
                    .setParameter("id", idUsuario)
                    .executeUpdate();

    }

    @Override
    public void asignarRol(Long idUsuario, ROL_USUARIO nuevoRol) {
         Session session = sessionFactory.getCurrentSession();
            String hql = "update Usuario set rol = :rol where id = :id";
            session.createQuery(hql)
                    .setParameter("rol", nuevoRol)
                    .setParameter("id", idUsuario)
                    .executeUpdate();

    }
    @Override
    public boolean usuarioTieneAvatar(Long idUsuario, Long idAvatar) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<UsuarioAvatar> root = query.from(UsuarioAvatar.class);

        query.select(cb.count(root));
        query.where(
                cb.and(
                        cb.equal(root.get("usuario").get("id"), idUsuario),
                        cb.equal(root.get("avatar").get("id"), idAvatar)
                )
        );

        Long count = session.createQuery(query).getSingleResult();
        return count > 0;
    }

    @Override
    public List<Long> obtenerIdsAvataresDelUsuario(Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<UsuarioAvatar> root = query.from(UsuarioAvatar.class);

        query.select(root.get("avatar").get("id"))
                .where(builder.equal(root.get("usuario").get("id"), idUsuario));

        return session.createQuery(query).getResultList();
    }
}