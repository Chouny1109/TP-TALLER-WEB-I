package com.tallerwebi.service.impl;

import com.tallerwebi.model.*;
import com.tallerwebi.repository.RepositorioAvatar;
import com.tallerwebi.repository.RepositorioTienda;
import com.tallerwebi.repository.RepositorioUsuario;
import com.tallerwebi.repository.impl.RepositorioTiendaImpl;
import com.tallerwebi.service.ServicioTienda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ServicioTiendaImpl implements ServicioTienda {
    
    private final RepositorioTienda repositorioTienda;
    private final RepositorioAvatar repositorioAvatar;
    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioTiendaImpl(RepositorioTienda respositorioTienda, RepositorioAvatar repositorioAvatar, RepositorioUsuario repositorioUsuario) {
        this.repositorioTienda = respositorioTienda;
        this.repositorioAvatar = repositorioAvatar;
        this.repositorioUsuario = repositorioUsuario;
    }
    
    @Override
    public List<Trampa> obtenerTrampas() {
        return repositorioTienda.obtenerTrampas();
    }

    @Override
    public List<Vida> obtenerVidas() {
        return repositorioTienda.obtenerVidas();
    }

    @Override
    public List<Moneda> obtenerMonedas() {
        return repositorioTienda.obtenerMonedas();
    }

    @Override
    public List<Avatar> obtenerAvatares() {
        return repositorioTienda.obtenerAvatares();
    }

    @Override
    public Moneda obtenerMonedaPorId(Long id) {
        return repositorioTienda.obtenerMonedaPorId(id);
    }

    @Override
    public Trampa obtenerTrampaPorId(Long id) {
        return repositorioTienda.obtenerTrampaPorId(id);
    }

    @Override
    public Avatar obtenerAvatar(Long id) {
        return repositorioAvatar.obtenerAvatar(id);
    }

    @Override
    public void asignarAvatarAUsuario(Usuario usuario, Avatar avatar) {
        UsuarioAvatar ua = new UsuarioAvatar();
        ua.setUsuario(usuario);
        ua.setAvatar(avatar);
        repositorioUsuario.asignarAvatarPorDefecto(ua);
    }

    @Override
    public Vida obtenerVidaPorId(Long id) {
        return repositorioTienda.obtenerVidaPorId(id);
    }
}
