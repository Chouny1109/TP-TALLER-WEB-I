package com.tallerwebi.repository;

import com.tallerwebi.model.TrampaReclamada;
import com.tallerwebi.model.Trampa;
import com.tallerwebi.model.Usuario;

import java.time.LocalDate;

public interface RepositorioTrampaReclamada {
    boolean yaReclamo(Usuario usuario, Trampa trampa, LocalDate fecha);
    void guardar(TrampaReclamada reclamacion);
}
