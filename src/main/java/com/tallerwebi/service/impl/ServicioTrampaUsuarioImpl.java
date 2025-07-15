package com.tallerwebi.service.impl;

import com.tallerwebi.model.*;
import com.tallerwebi.repository.RepositorioTrampaReclamada;
import com.tallerwebi.repository.RepositorioTrampaUsuario;
import com.tallerwebi.service.ServicioTrampaUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("servicioTrampaUsuario")
@Transactional
public class ServicioTrampaUsuarioImpl implements ServicioTrampaUsuario {

    private final RepositorioTrampaUsuario repositorioTrampaUsuario;
    private RepositorioTrampaReclamada repositorioTrampaReclamada;


    @Autowired
    public ServicioTrampaUsuarioImpl(RepositorioTrampaUsuario repositorioTrampaUsuario, RepositorioTrampaReclamada repositorioTrampaReclamada) {
        this.repositorioTrampaUsuario = repositorioTrampaUsuario;
        this.repositorioTrampaReclamada = repositorioTrampaReclamada;
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
    public List<TrampaUsuario> obtenerTrampasDelUsuario(Long idUsuario) {
        return repositorioTrampaUsuario.obtenerTrampasDelUsuario(idUsuario);
    }


    @Override
    public Map<String, Trampa> obtenerTrampasPorDia() {
        List<TrampaDia> relaciones = repositorioTrampaUsuario.obtenerTrampasPorDia();

        return relaciones.stream().collect(Collectors.toMap(
                TrampaDia::getDia,
                TrampaDia::getTrampa
        ));
    }

    @Override
    public boolean reclamarTrampaDelDia(Usuario usuario, Trampa trampa) {
        LocalDate hoy = LocalDate.now();

        boolean yaReclamo = repositorioTrampaReclamada.yaReclamo(usuario, trampa, hoy);
        if (yaReclamo) {
            return false;
        }

        asignarTrampaAUsuario(usuario, trampa);

        TrampaReclamada trampaReclamada = new TrampaReclamada(usuario, trampa, hoy);
        repositorioTrampaReclamada.guardar(trampaReclamada);

        return true;
    }

    @Override
    public void consumirTrampa(Long idUsuario, Long idTrampa) {
        TrampaUsuario tu = repositorioTrampaUsuario.buscarPorUsuarioYTrampa(idUsuario, idTrampa);
        if (tu != null && tu.getCantidad() > 0) {
            tu.setCantidad(tu.getCantidad() - 1);
            repositorioTrampaUsuario.modificar(tu);
        }
    }

}
