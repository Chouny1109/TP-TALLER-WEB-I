package com.tallerwebi.service;
import com.tallerwebi.model.*;
import java.util.List;

public interface ServicioMisTrampas {
    List<Trampa> obtenerTrampasPorUsuario(Usuario usuario);
}
