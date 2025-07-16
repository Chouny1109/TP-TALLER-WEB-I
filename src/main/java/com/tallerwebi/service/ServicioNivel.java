package com.tallerwebi.service;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Nivel;
import com.tallerwebi.model.NivelUsuarioDTO;
import com.tallerwebi.model.Usuario;
import org.springframework.transaction.annotation.Transactional;

public interface ServicioNivel {

    Nivel obtenerNivelPorExperiencia(Integer experiencia);

    NivelUsuarioDTO construirInfoDeNivel(Usuario usuario);

    void verificarSiSubeDeNivel(Usuario usuario);

    Nivel obtenerNivelSiguiente(Integer experiencia);

    void otorgarExperiencia(Usuario usuario, Mision mision);
}
