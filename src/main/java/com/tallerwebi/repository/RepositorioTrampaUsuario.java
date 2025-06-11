package com.tallerwebi.repository;

import com.tallerwebi.model.TrampaUsuario;
import java.util.List;

public interface RepositorioTrampaUsuario {
    void guardar(TrampaUsuario trampaUsuario);
    List<TrampaUsuario> obtenerTrampasDelUsuario(Long idUsuario);
}