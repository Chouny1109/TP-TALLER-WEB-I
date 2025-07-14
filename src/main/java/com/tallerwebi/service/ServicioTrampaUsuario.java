package com.tallerwebi.service;

import com.tallerwebi.model.Trampa;
import com.tallerwebi.model.TrampaUsuario;
import com.tallerwebi.model.Usuario;

import java.util.List;
import java.util.Map;

public interface ServicioTrampaUsuario {
    void asignarTrampaAUsuario(Usuario usuario, Trampa trampa);
    List<TrampaUsuario> obtenerTrampasDelUsuario(Long idUsuario);
    Map<String, Trampa> obtenerTrampasPorDia();
    boolean reclamarTrampaDelDia(Usuario usuario, Trampa trampa);
}