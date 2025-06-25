package com.tallerwebi.repository.impl;

import com.tallerwebi.model.SolicitudAmistad;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioSolicitudAmistad;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioSolicitudAmistad")
public class RepositorioSolicitudAmistadImpl implements RepositorioSolicitudAmistad {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioSolicitudAmistadImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void guardar(SolicitudAmistad solicitud) {
        getSession().save(solicitud);
    }

    @Override
    public void eliminar(SolicitudAmistad solicitud) {
        getSession().delete(solicitud);
    }

    @Override
    public SolicitudAmistad buscarPorRemitenteYDestinatario(Usuario remitente, Usuario destinatario) {
        return (SolicitudAmistad) getSession()
                .createCriteria(SolicitudAmistad.class)
                .add(Restrictions.eq("remitente", remitente))
                .add(Restrictions.eq("destinatario", destinatario))
                .uniqueResult();
    }

    @Override
    public List<SolicitudAmistad> buscarSolicitudesRecibidas(Usuario destinatario) {
        return getSession()
                .createCriteria(SolicitudAmistad.class)
                .add(Restrictions.eq("destinatario", destinatario))
                .list();
    }

    @Override
    public List<SolicitudAmistad> buscarSolicitudesEnviadas(Usuario remitente) {
        return getSession()
                .createCriteria(SolicitudAmistad.class)
                .add(Restrictions.eq("remitente", remitente))
                .list();
    }

    @Override
    public SolicitudAmistad buscarPorId(Long id) {
        return (SolicitudAmistad) getSession().get(SolicitudAmistad.class, id);
    }

}