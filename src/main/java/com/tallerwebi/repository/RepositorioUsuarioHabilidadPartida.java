package com.tallerwebi.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface RepositorioUsuarioHabilidadPartida {
    boolean obtenerHabilidadesUsadasParaLaFecha(Long id, LocalDateTime fecha);
    boolean elUsuarioTienePartidasGanadasSinUsarHabilidades(Long id, LocalDateTime fecha);
}
