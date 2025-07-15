package com.tallerwebi.service;

import com.tallerwebi.model.Mision;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ServicioMisiones {
    List<Mision> obtenerMisionesDelSistema();

}
