package com.tallerwebi.strategys.Mision;

import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMision;

public interface EstrategiaMision {
    void completarMision(Usuario usuario, UsuarioMision usuarioMision);
}