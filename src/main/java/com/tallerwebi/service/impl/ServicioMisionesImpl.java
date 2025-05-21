package com.tallerwebi.service.impl;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.service.ServicioMisiones;
import org.hsqldb.lib.Collection;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ServicioMisionesImpl implements ServicioMisiones {

    private RepositorioUsuario repositorioUsuario;

    public ServicioMisionesImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public List<Mision> obtenerLasMisionesDelUsuario(Long id) {
        Usuario buscado = this.repositorioUsuario.buscarUsuarioPorId(id);
        if (buscado != null) {
            return buscado.getMisiones();
        }
        return Collections.emptyList();
    }
}
