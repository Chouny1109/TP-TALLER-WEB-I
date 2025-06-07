package com.tallerwebi.repository;

import com.tallerwebi.model.Mision;
import com.tallerwebi.model.Usuario;

import java.util.List;

public interface RepositorioUsuario {

//    Usuario buscarUsuario(String email, String password);
    Boolean guardar(Usuario usuario);
    Usuario buscar(String email);
    void modificar(Usuario usuario);
    Usuario buscarUsuarioPorId(Long id);
    List<Mision> obtenerMisiones(Long id);
}

