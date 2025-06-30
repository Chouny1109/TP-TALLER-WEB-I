package com.tallerwebi.repository;

import com.tallerwebi.dominio.enums.TIPO_MISION;
import com.tallerwebi.model.TipoDeMision;

import java.util.List;

public interface TipoDeMisionRepository {

    boolean existePorNombre(TIPO_MISION tipo);

    void save(TipoDeMision tipoMision);

}
