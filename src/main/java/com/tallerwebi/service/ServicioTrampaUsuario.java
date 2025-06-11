package com.tallerwebi.service;

import com.tallerwebi.model.Trampa;
import com.tallerwebi.model.Usuario;

import java.util.List;

public interface ServicioTrampaUsuario {
    void asignarTrampaAUsuario(Usuario usuario, Trampa trampa);
    List<Trampa> obtenerTrampasDelUsuario(Long idUsuario);
}