package com.tallerwebi.service.impl;

import com.tallerwebi.model.Trampa;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioTrampas;
import com.tallerwebi.service.ServicioMisTrampas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioMisTrampasImpl implements ServicioMisTrampas {

    @Autowired
    private RepositorioTrampas repositorioTrampas;

    @Override
    public List<Trampa> obtenerTrampasPorUsuario(Usuario usuario) {
        return repositorioTrampas.buscarTrampasPorUsuario(usuario.getId());
    }
}
