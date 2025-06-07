package com.tallerwebi.service.impl;

import com.tallerwebi.model.*;
import com.tallerwebi.repository.RepositorioTienda;
import com.tallerwebi.repository.impl.RepositorioTiendaImpl;
import com.tallerwebi.service.ServicioTienda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioTiendaImpl implements ServicioTienda {
    
    private final RepositorioTienda repositorioTienda;

    @Autowired
    public ServicioTiendaImpl(RepositorioTienda respositorioTienda) {
        this.repositorioTienda = respositorioTienda;
    }
    
    @Override
    public List<Trampa> obtenerTrampas() {
        return repositorioTienda.obtenerTrampas();
    }

    @Override
    public List<Vida> obtenerVidas() {
        return repositorioTienda.obtenerVidas();
    }

    @Override
    public List<Moneda> obtenerMonedas() {
        return repositorioTienda.obtenerMonedas();
    }

    @Override
    public List<Avatar> obtenerAvatares() {
        return repositorioTienda.obtenerAvatares();
    }
}
