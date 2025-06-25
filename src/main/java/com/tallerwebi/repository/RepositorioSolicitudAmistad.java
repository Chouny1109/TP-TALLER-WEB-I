package com.tallerwebi.repository;

import com.tallerwebi.model.SolicitudAmistad;
import com.tallerwebi.model.Usuario;

import java.util.List;

public interface RepositorioSolicitudAmistad {

    void guardar(SolicitudAmistad solicitud);

    void eliminar(SolicitudAmistad solicitud);

    SolicitudAmistad buscarPorRemitenteYDestinatario(Usuario remitente, Usuario destinatario);

    List<SolicitudAmistad> buscarSolicitudesRecibidas(Usuario destinatario);

    List<SolicitudAmistad> buscarSolicitudesEnviadas(Usuario remitente);

    SolicitudAmistad buscarPorId(Long id);

}