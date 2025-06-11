package com.tallerwebi.repository;

import com.tallerwebi.model.Mision;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RepositorioMisiones {
    List<Mision> obtenerMisiones();
}
