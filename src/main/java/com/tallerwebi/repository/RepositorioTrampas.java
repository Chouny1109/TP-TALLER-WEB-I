package com.tallerwebi.repository;

import com.tallerwebi.model.Trampa;
import java.util.List;

public interface RepositorioTrampas {
    List<Trampa> buscarTrampasPorUsuario(Long idUsuario);
}
