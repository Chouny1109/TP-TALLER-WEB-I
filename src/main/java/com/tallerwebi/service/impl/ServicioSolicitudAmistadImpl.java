package com.tallerwebi.service.impl;

import com.tallerwebi.model.SolicitudAmistad;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioSolicitudAmistad;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioSolicitudAmistad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioSolicitudAmistad")
@Transactional
public class ServicioSolicitudAmistadImpl implements ServicioSolicitudAmistad {

    private final RepositorioSolicitudAmistad repositorioSolicitudAmistad;
    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioSolicitudAmistadImpl(RepositorioSolicitudAmistad repositorioSolicitudAmistad,
                                        RepositorioUsuario repositorioUsuario) {
        this.repositorioSolicitudAmistad = repositorioSolicitudAmistad;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public void enviarSolicitud(Usuario remitente, Usuario destinatario) {
        SolicitudAmistad existente = repositorioSolicitudAmistad.buscarPorRemitenteYDestinatario(remitente, destinatario);
        if (existente == null) {
            SolicitudAmistad nueva = new SolicitudAmistad(remitente, destinatario);
            repositorioSolicitudAmistad.guardar(nueva);
        }
    }

    @Override
    public void aceptarSolicitud(SolicitudAmistad solicitud) {
        Usuario remitente = repositorioUsuario.buscarUsuarioPorId(solicitud.getRemitente().getId());
        Usuario destinatario = repositorioUsuario.buscarUsuarioPorId(solicitud.getDestinatario().getId());

        remitente.getAmigos().add(destinatario);
        destinatario.getAmigos().add(remitente);

        repositorioUsuario.modificar(remitente);
        repositorioUsuario.modificar(destinatario);

        repositorioSolicitudAmistad.eliminar(solicitud);
    }

    @Override
    public void rechazarSolicitud(SolicitudAmistad solicitud) {
        repositorioSolicitudAmistad.eliminar(solicitud);
    }

    @Override
    public List<SolicitudAmistad> obtenerSolicitudesRecibidas(Usuario destinatario) {
        return repositorioSolicitudAmistad.buscarSolicitudesRecibidas(destinatario);
    }

    @Override
    public List<SolicitudAmistad> obtenerSolicitudesEnviadas(Usuario remitente) {
        return repositorioSolicitudAmistad.buscarSolicitudesEnviadas(remitente);
    }

    @Override
    public SolicitudAmistad buscarPorRemitenteYDestinatario(Usuario remitente, Usuario destinatario) {
        return repositorioSolicitudAmistad.buscarPorRemitenteYDestinatario(remitente, destinatario);
    }

    @Override
    public SolicitudAmistad buscarPorId(Long id) {
        return repositorioSolicitudAmistad.buscarPorId(id);
    }
}