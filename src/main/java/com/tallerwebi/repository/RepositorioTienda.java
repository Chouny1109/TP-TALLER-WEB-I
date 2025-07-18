package com.tallerwebi.repository;

import com.tallerwebi.model.*;

import java.util.List;

public interface RepositorioTienda {
    List<Trampa> obtenerTrampas();
    List<Vida> obtenerVidas();
    List<Moneda> obtenerMonedas();
    List<Avatar> obtenerAvatares();
    Moneda obtenerMonedaPorId(Long id);
    Trampa obtenerTrampaPorId(Long id);
    Vida obtenerVidaPorId(Long id);
}
