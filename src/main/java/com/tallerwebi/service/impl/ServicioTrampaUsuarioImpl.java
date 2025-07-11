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
        TrampaUsuario existente = repositorioTrampaUsuario.buscarPorUsuarioYTrampa(usuario.getId(), trampa.getId());

        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + 1);
            repositorioTrampaUsuario.modificar(existente);
        } else {
            TrampaUsuario nuevo = new TrampaUsuario();
            nuevo.setUsuario(usuario);
            nuevo.setTrampa(trampa);
            nuevo.setCantidad(1);
            repositorioTrampaUsuario.guardar(nuevo);
        }
    }

    @Override
    public List<Trampa> obtenerTrampasDelUsuario(Long idUsuario) {
        return repositorioTrampaUsuario.obtenerTrampasDelUsuario(idUsuario).stream()
                .map(TrampaUsuario::getTrampa)
                .collect(Collectors.toList());
    }
}
