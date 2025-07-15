package com.tallerwebi.repository;

import java.time.LocalDate;

public interface RepositorioUsuarioHabilidadPartida {
    boolean obtenerHabilidadesUsadasParaLaFecha(Long id, LocalDate fecha);
    boolean elUsuarioTienePartidasGanadasSinUsarHabilidades(Long id,LocalDate fecha);
}
