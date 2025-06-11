package com.tallerwebi.service.impl;

import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;
import com.tallerwebi.repository.RepositorioMisionUsuario;
import com.tallerwebi.repository.RepositorioMisiones;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioMisiones;
import org.hsqldb.lib.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServicioMisionesImpl implements ServicioMisiones {

    private final RepositorioMisiones repositorioMisiones;

    public ServicioMisionesImpl(RepositorioMisiones repositorioMisiones) {
        this.repositorioMisiones = repositorioMisiones;
    }

    @Override
    public List<Mision> obtenerMisionesDelSistema() {
        return this.repositorioMisiones.obtenerMisiones();
    }
}
