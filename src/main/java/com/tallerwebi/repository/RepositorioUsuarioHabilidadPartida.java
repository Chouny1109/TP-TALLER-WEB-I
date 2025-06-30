package com.tallerwebi.repository;

import java.time.LocalDate;

public interface RepositorioUsuarioHabilidadPartida {
    long obtenerHabilidadesUsadasParaLaFecha(Long id, LocalDate fecha);
    boolean elUsuarioTienePartidasGanadasSinUsarHabilidades(Long id);
}
