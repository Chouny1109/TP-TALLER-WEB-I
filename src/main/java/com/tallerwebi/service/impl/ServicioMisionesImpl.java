package com.tallerwebi.service.impl;

import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioMisiones;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioMisiones;
import org.hsqldb.lib.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ServicioMisionesImpl implements ServicioMisiones {

    private final RepositorioMisiones repositorioMisiones;
    private final RepositorioUsuario repositorioUsuario;

    public ServicioMisionesImpl(RepositorioMisiones repositorioMisiones, RepositorioUsuario repositorioUsuario) {
        this.repositorioMisiones = repositorioMisiones;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public List<Mision> obtenerLasMisionesDelUsuario(Long id) throws UsuarioNoExistente {
        Usuario buscado = repositorioUsuario.buscarUsuarioPorId(id);

        if (buscado == null) {
            throw new UsuarioNoExistente();
        }

        return repositorioMisiones.misionesDeUsuario(id);
    }

    @Override
    public void asignarMisionesDiarias() {


    }
}
