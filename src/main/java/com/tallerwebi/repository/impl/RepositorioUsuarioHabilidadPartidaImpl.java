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
import java.util.List;

@Repository
public class RepositorioUsuarioHabilidadPartidaImpl implements RepositorioUsuarioHabilidadPartida {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioHabilidadPartidaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean obtenerHabilidadesUsadasParaLaFecha(Long id, LocalDate fecha) {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<UsuarioHabilidadPartida> root = query.from(UsuarioHabilidadPartida.class);

        // Agrupamos por partida
        query.multiselect(root.get("partida").get("id"));
        query.where(
                cb.equal(root.get("usuario").get("id"), id),
                cb.equal(root.get("fecha"), fecha)
        );
        query.groupBy(root.get("partida").get("id"));
        query.having(cb.greaterThanOrEqualTo(cb.countDistinct(root.get("habilidad").get("id")), 2L));

        List<Long> resultados = sessionFactory.getCurrentSession().createQuery(query).getResultList();

        // Si hay al menos una partida con 2 o más habilidades distintas
        return !resultados.isEmpty();
    }

    @Override
    public boolean elUsuarioTienePartidasGanadasSinUsarHabilidades(Long id, LocalDate fecha) {
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
        Predicate fechaCoincide = builder.equal(root.get("fecha"), fecha); // 👈 Acá se filtra por fecha


        query.select(builder.count(root))
                .where(esUsuario, esPartidaGanada, noUsoHabilidades, fechaCoincide);
        Long cantidad = sessionFactory.getCurrentSession().createQuery(query).getSingleResult();

        return cantidad > 0;
    }

}
