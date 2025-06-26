package com.tallerwebi.repository;

import com.tallerwebi.model.Usuario;

import java.util.List;

public interface RepositorioAdmin {

    void AplicarPenalizacionAUsuario(int CantidadDias);
    List<Usuario> ListaDeUsuarios();

}
