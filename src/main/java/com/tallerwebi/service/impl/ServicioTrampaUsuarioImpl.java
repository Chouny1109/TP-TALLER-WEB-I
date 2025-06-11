package com.tallerwebi.service.impl;

import com.tallerwebi.model.Trampa;
import com.tallerwebi.model.TrampaUsuario;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioTrampaUsuario;
import com.tallerwebi.service.ServicioTrampaUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service("servicioTrampaUsuario")
@Transactional
public class ServicioTrampaUsuarioImpl implements ServicioTrampaUsuario {

    private final RepositorioTrampaUsuario repositorioTrampaUsuario;

    @Autowired
    public ServicioTrampaUsuarioImpl(RepositorioTrampaUsuario repositorioTrampaUsuario) {
        this.repositorioTrampaUsuario = repositorioTrampaUsuario;
    }

    @Override
    public void asignarTrampaAUsuario(Usuario usuario, Trampa trampa) {
        TrampaUsuario tu = new TrampaUsuario();
        tu.setUsuario(usuario);
        tu.setTrampa(trampa);
        repositorioTrampaUsuario.guardar(tu);
    }

    @Override
    public List<Trampa> obtenerTrampasDelUsuario(Long idUsuario) {
        return repositorioTrampaUsuario.obtenerTrampasDelUsuario(idUsuario).stream()
                .map(TrampaUsuario::getTrampa)
                .collect(Collectors.toList());
    }
}
