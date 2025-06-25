package com.tallerwebi.service;

import com.tallerwebi.model.SolicitudAmistad;
import com.tallerwebi.model.Usuario;

import java.util.List;

public interface ServicioSolicitudAmistad {

    void enviarSolicitud(Usuario remitente, Usuario destinatario);

    void aceptarSolicitud(SolicitudAmistad solicitud);

    void rechazarSolicitud(SolicitudAmistad solicitud);

    List<SolicitudAmistad> obtenerSolicitudesRecibidas(Usuario destinatario);

    List<SolicitudAmistad> obtenerSolicitudesEnviadas(Usuario remitente);

    SolicitudAmistad buscarPorRemitenteYDestinatario(Usuario remitente, Usuario destinatario);

    SolicitudAmistad buscarPorId(Long id);
}