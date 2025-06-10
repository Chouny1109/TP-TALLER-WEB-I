package com.tallerwebi.service.impl;

import com.tallerwebi.model.Amigo;
import com.tallerwebi.repository.RepositorioAmigo;
import com.tallerwebi.service.ServicioAmigos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioAmigos")
@Transactional

public class ServicioAmigosImpl implements ServicioAmigos {

    private final RepositorioAmigo repositorioAmigo;

    @Autowired
    public ServicioAmigosImpl(RepositorioAmigo repositorioAmigo) {
        this.repositorioAmigo = repositorioAmigo;
    }

    @Override
    public List<Amigo> obtenerAmigos() {
        return repositorioAmigo.obtenerAmigos();
    }
}
