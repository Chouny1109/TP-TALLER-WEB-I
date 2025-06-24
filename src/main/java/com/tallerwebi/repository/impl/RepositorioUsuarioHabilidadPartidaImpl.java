package com.tallerwebi.repository.impl;

import com.tallerwebi.dominio.enums.ESTADO_PARTIDA_JUGADOR;
import com.tallerwebi.model.UsuarioHabilidadPartida;
import com.tallerwebi.model.UsuarioPartida;
import com.tallerwebi.repository.RepositorioUsuarioHabilidadPartida;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.time.LocalDate;

@Repository
public class RepositorioUsuarioHabilidadPartidaImpl implements RepositorioUsuarioHabilidadPartida {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioHabilidadPartidaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public long obtenerHabilidadesUsadasParaLaFecha(Long id, LocalDate fecha) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<UsuarioHabilidadPartida> root = query.from(UsuarioHabilidadPartida.class);

        query.select(builder.countDistinct(root.get("habilidad")))
                .where(
                        builder.equal(root.get("usuario").get("id"), id),
                        builder.equal(root.get("fecha"), fecha)
                );

        return sessionFactory.getCurrentSession().createQuery(query).getSingleResult();
    }

    @Override
    public boolean elUsuarioTienePartidasGanadasSinUsarHabilidades(Long id) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<UsuarioPartida> root = query.from(UsuarioPartida.class);

        Subquery<Long> subQuery = query.subquery(Long.class);
        Root<UsuarioHabilidadPartida> subRoot = subQuery.from(UsuarioHabilidadPartida.class);
        subQuery.select(subRoot.get("partida").get("id"))
                .where(
                        builder.equal(subRoot.get("usuario").get("id"), id)
                );

        Predicate esUsuario = builder.equal(root.get("usuario").get("id"), id);
        Predicate esPartidaGanada = builder.equal(root.get("estado"), ESTADO_PARTIDA_JUGADOR.VICTORIA);
        Predicate noUsoHabilidades = builder.not(root.get("partida").get("id").in(subQuery));

        query.select(builder.count(root))
                .where(esUsuario, esPartidaGanada, noUsoHabilidades);
        Long cantidad = sessionFactory.getCurrentSession().createQuery(query).getSingleResult();

        return cantidad > 0;
    }

}
