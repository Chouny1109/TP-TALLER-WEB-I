package com.tallerwebi.repository;

import com.tallerwebi.model.TrampaDia;
import com.tallerwebi.model.TrampaUsuario;
import java.util.List;

public interface RepositorioTrampaUsuario {
    void guardar(TrampaUsuario trampaUsuario);
    List<TrampaUsuario> obtenerTrampasDelUsuario(Long idUsuario);
    TrampaUsuario buscarPorUsuarioYTrampa(Long idUsuario, Long idTrampa);
    void modificar(TrampaUsuario trampaUsuario);
    List<TrampaDia> obtenerTrampasPorDia();

}