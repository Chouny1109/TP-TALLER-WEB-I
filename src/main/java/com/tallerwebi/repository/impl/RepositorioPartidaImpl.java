package com.tallerwebi.repository.impl;

import com.tallerwebi.dominio.enums.ESTADO_PARTIDA;
import com.tallerwebi.dominio.enums.TIPO_PARTIDA;
import com.tallerwebi.model.*;
import com.tallerwebi.repository.RepositorioPartida;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository("repositorioPartida")
@Transactional
public class RepositorioPartidaImpl implements RepositorioPartida {
    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioPartidaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Partida> obtenerPartidasAbiertasConTurnoEnNull(TIPO_PARTIDA modoJuego, Usuario jugador) {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria(Partida.class, "p")
                .add(Restrictions.eq("estadoPartida", ESTADO_PARTIDA.ABIERTA))
                .add(Restrictions.isNull("turnoActual"))
                .add(Restrictions.eq("tipo", modoJuego));

        // Subquery para UsuarioPartida
        DetachedCriteria subquery = DetachedCriteria.forClass(UsuarioPartida.class, "up")
                .add(Restrictions.eq("up.usuario", jugador))
                .add(Restrictions.eqProperty("up.partida.id", "p.id"))
                .setProjection(Projections.id());

        // Agrego condición NOT EXISTS para excluir partidas donde el jugador ya está
        criteria.add(Subqueries.notExists(subquery));

        // Lock para evitar condiciones de carrera
        criteria.setLockMode(LockMode.PESSIMISTIC_WRITE);

        return criteria.list();
    }


    @Override
    public Boolean guardarPartida(Partida partida) {
        sessionFactory.getCurrentSession().save(partida);
        return true;
    }

    @Override
    public void actualizarPartida(Partida partida) {
        sessionFactory.getCurrentSession().merge(partida);

    }

    @Override
    public List<Partida> obtenerPartidasAbiertaPorModo(TIPO_PARTIDA tipo) {
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Partida.class)
                .add(Restrictions.eq("estadoPartida", ESTADO_PARTIDA.ABIERTA))
                .add(Restrictions.eq("tipo", tipo))
                .setLockMode(LockMode.PESSIMISTIC_WRITE) // <-- Aquí el lock
                .list();
    }



    @Override
    public void agregarUsuarioPartidaRelacion(UsuarioPartida usuarioPartida) {
        sessionFactory.getCurrentSession().save(usuarioPartida);
    }

    @Override
    public Usuario obtenerRivalDePartida(Long idPartida, Long idJugadorActual) {
        Session session = sessionFactory.getCurrentSession();

        UsuarioPartida usuarioPartida = (UsuarioPartida) session.createCriteria(UsuarioPartida.class)
                .createAlias("partida", "p")
                .createAlias("usuario", "u")
                .add(Restrictions.eq("p.id", idPartida))
                .add(Restrictions.ne("u.id", idJugadorActual))  // distinto al jugador actual
                .setMaxResults(1)
                .uniqueResult();

        return usuarioPartida != null ? usuarioPartida.getUsuario() : null;
    }

    @Override
    public List<Usuario> obtenerJugadoresDePartida(Long idPartida) {
        Session session = sessionFactory.getCurrentSession();

        List<UsuarioPartida> listaUP = session.createCriteria(UsuarioPartida.class)
                .createAlias("partida", "p")
                .createAlias("usuario", "u")
                .add(Restrictions.eq("p.id", idPartida))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();

        return listaUP.stream()
                .map(UsuarioPartida::getUsuario)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean jugadorEstaJugando(Long idJugador) {
        Session session = sessionFactory.getCurrentSession();

        UsuarioPartida usuarioPartida = (UsuarioPartida) session.createCriteria(UsuarioPartida.class, "up")
                .createAlias("up.usuario", "u")
                .createAlias("up.partida", "p")
                .add(Restrictions.eq("u.id", idJugador))
                .add(Restrictions.or(
                        Restrictions.eq("p.estadoPartida", ESTADO_PARTIDA.ABIERTA),
                        Restrictions.eq("p.estadoPartida", ESTADO_PARTIDA.EN_CURSO)
                ))
                .setMaxResults(1)
                .uniqueResult();

        return usuarioPartida != null;
    }

    @Override
    public Partida obtenerPartidaActivaDeJugador(Long idJugador) {
        Session session = sessionFactory.getCurrentSession();

        UsuarioPartida usuarioPartida = (UsuarioPartida) session.createCriteria(UsuarioPartida.class, "up")
                .createAlias("up.usuario", "u")
                .createAlias("up.partida", "p")
                .add(Restrictions.eq("u.id", idJugador))
                .add(Restrictions.or(
                        Restrictions.eq("p.estadoPartida", ESTADO_PARTIDA.ABIERTA),
                        Restrictions.eq("p.estadoPartida", ESTADO_PARTIDA.EN_CURSO)
                ))
                .setMaxResults(1)
                .uniqueResult();

        if (usuarioPartida != null) {
            return usuarioPartida.getPartida();
        }
        return null;
    }

    @Override
    public Integer obtenerCantidadDePartidasJugadasParaLaFecha(Long id, LocalDate fecha) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<UsuarioPartida> root = query.from(UsuarioPartida.class);

        query.select(builder.countDistinct(root))
                .where(builder.equal(root.get("usuario").get("id"), id))
                .where(builder.equal(root.get("fecha").get("fecha"), fecha));

        return sessionFactory.getCurrentSession().createQuery(query).getSingleResult().intValue();
    }

    @Override
    public List<UsuarioPartida> obtenerLasPartidasDelUsuarioParaDeterminadaFecha(Long id, LocalDateTime fecha) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<UsuarioPartida> query = builder.createQuery(UsuarioPartida.class);
        Root<UsuarioPartida> root = query.from(UsuarioPartida.class);

        query.select(root)
                .where(
                        builder.and(
                                builder.equal(root.get("usuario").get("id"), id),
                                builder.equal(root.get("fecha"), fecha)
                        )
                )
                .orderBy(builder.asc(root.get("fecha")));  // <-- orden ascendente por fecha

        return sessionFactory.getCurrentSession().createQuery(query).getResultList();
    }



    @Override
    public void finalizarPartida(Long idPartida) {

        Session session = sessionFactory.getCurrentSession();
        Partida p = (Partida) session.createCriteria(Partida.class)
                .add(Restrictions.eq("id", idPartida))
                .uniqueResult();

        if (p != null) {
            p.setEstadoPartida(ESTADO_PARTIDA.FINALIZADA); // 2. Cambiar el estado
            session.update(p); // 3. Guardar los cambios
        }
    }

    @Override
    public Partida buscarPartidaPorId(Long idPartida) {
        Session session = sessionFactory.getCurrentSession();

        return (Partida) session.createCriteria(Partida.class)
                .add(Restrictions.eq("id", idPartida))
                .uniqueResult();

    }

    @Override
    public ResultadoRespuesta obtenerResultadoRespuestaEnPartidaPorJugador(Long idPartida, Usuario jugador) {
        Session session = sessionFactory.getCurrentSession();

        return (ResultadoRespuesta) session.createCriteria(ResultadoRespuesta.class)
                .createAlias("partida", "p")
                .createAlias("usuario", "u")
                .add(Restrictions.eq("p.id", idPartida))
                .add(Restrictions.eq("u.id", jugador.getId()))
                .uniqueResult();
    }



    @Override
    public List<ResultadoRespuesta> obtenerResultadoRespuestaDeJugadoresEnPartida(Long idPartida){
        Session session = sessionFactory.getCurrentSession();
        return (List<ResultadoRespuesta>) session.createCriteria(ResultadoRespuesta.class)
                .createAlias("partida", "p")
                .add(Restrictions.eq("p.id", idPartida))
                .list();
    }
    @Override
    public void guardarResultadoRespuesta(ResultadoRespuesta resultadoRespuesta) {
        sessionFactory.getCurrentSession().save(resultadoRespuesta);
    }
    @Override
    public void actualizarResultadoRespuesta(ResultadoRespuesta resultadoRespuesta) {
        sessionFactory.getCurrentSession().update(resultadoRespuesta);
    }

    @Override
    public boolean existeUsuarioRespondePregunta(Long idUsuario, Long idPregunta) {
        String hql = "select count(urp.id) from UsuarioRespondePregunta urp where urp.usuario.id = :idUsuario and urp.pregunta.id = :idPregunta";
        Long count = (Long) sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("idUsuario", idUsuario)
                .setParameter("idPregunta", idPregunta)
                .uniqueResult();
        return count != null && count > 0;
    }


    @Override
    public void guardarUsuarioRespondePregunta(UsuarioRespondePregunta usp){
        sessionFactory.getCurrentSession().save(usp);
    }
    @Override
    public ResultadoRespuesta obtenerUltimoResultadoRespuestaEnPartidaPorJugador(Long idPartida, Usuario jugador) {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
                        "FROM ResultadoRespuesta r WHERE r.partida.id = :idPartida AND r.usuario.id = :idUsuario ORDER BY r.orden DESC",
                        ResultadoRespuesta.class
                )
                .setParameter("idPartida", idPartida)
                .setParameter("idUsuario", jugador.getId())
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public ResultadoRespuesta obtenerUltimoResultadoRespuestaEnPartidaDeRival(Long idPartida, Usuario jugador) {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
                        "FROM ResultadoRespuesta r WHERE r.partida.id = :idPartida AND r.usuario.id <> :idUsuario ORDER BY r.orden DESC",
                        ResultadoRespuesta.class
                )
                .setParameter("idPartida", idPartida)
                .setParameter("idUsuario", jugador.getId())
                .setMaxResults(1)
                .uniqueResult();
    }


    @Override
    public SiguientePreguntaSupervivencia obtenerSiguientePreguntaEntidad(Partida partida, Integer orden) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "select sp from SiguientePreguntaSupervivencia sp " +
                "join fetch sp.siguientePregunta p " +
                "where sp.partida.id = :idPartida and sp.orden = :orden";

        return session.createQuery(hql, SiguientePreguntaSupervivencia.class)
                .setParameter("idPartida", partida.getId())
                .setParameter("orden", orden)
                .setMaxResults(1)
                .uniqueResult();
    }



    @Override
    public void guardarSiguientePregunta(SiguientePreguntaSupervivencia siguientePregunta) {
        sessionFactory.getCurrentSession().save(siguientePregunta);
    }

    @Override
    public Integer obtenerMaxOrdenSiguientePregunta(Long idPartida) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT MAX(s.orden) FROM SiguientePreguntaSupervivencia s WHERE s.partida.id = :idPartida",
                        Integer.class)
                .setParameter("idPartida", idPartida)
                .uniqueResult();
    }


    @PersistenceContext
    private EntityManager em;

    @Override
    public ResultadoRespuesta obtenerResultadoPorOrdenYPregunta(Long idPartida, Usuario usuario, int orden, Pregunta pregunta) {
        String jpql = "SELECT rr FROM ResultadoRespuesta rr " +
                "WHERE rr.partida.id = :idPartida " +
                "AND rr.usuario = :usuario " +
                "AND rr.orden = :orden " +
                "AND rr.pregunta = :pregunta";

        try {
            return em.createQuery(jpql, ResultadoRespuesta.class)
                    .setParameter("idPartida", idPartida)
                    .setParameter("usuario", usuario)
                    .setParameter("orden", orden)
                    .setParameter("pregunta", pregunta)  // entidad pregunta
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public ResultadoRespuesta obtenerResultadoPorPartidaUsuarioYPregunta(Long idPartida, Usuario usuario, Pregunta preguntaResp) {
        Session session = sessionFactory.getCurrentSession();

        List<ResultadoRespuesta> resultados = session.createQuery(
                        "FROM ResultadoRespuesta r WHERE r.partida.id = :idPartida AND r.usuario.id = :idUsuario AND r.pregunta.id = :idPregunta",
                        ResultadoRespuesta.class)
                .setParameter("idPartida", idPartida)
                .setParameter("idUsuario", usuario.getId())
                .setParameter("idPregunta", preguntaResp.getId())
                .getResultList();

        if (resultados.isEmpty()) {
            return null;
        } else {
            return resultados.get(0);
        }
    }

    @Override
    public CategoriasGanadasEnPartida obtenerCategoriasGanadasDeUsuarioEnPartida(Partida partida, Usuario usuario) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "SELECT c FROM CategoriasGanadasEnPartida c LEFT JOIN FETCH c.categoriasGanadas WHERE c.partida = :partida AND c.usuario = :usuario";
        return session.createQuery(hql, CategoriasGanadasEnPartida.class)
                .setParameter("partida", partida)
                .setParameter("usuario", usuario)
                .uniqueResult();
    }


    @Override
    public void actualizarCategoriasGanadas(CategoriasGanadasEnPartida cat) {
        sessionFactory.getCurrentSession().update(cat);
    }

    @Override
    public void guardarCategoriasGanadas(CategoriasGanadasEnPartida cat) {
        sessionFactory.getCurrentSession().save(cat);
    }


    @Override
    public List<Partida> obtenerPartidasAbiertasOEnCursoMultijugadorDeUnJugador(Usuario u){
        Session session = sessionFactory.getCurrentSession();

        // Hacemos un join desde UsuarioPartida para filtrar partidas por usuario
        Criteria criteria = session.createCriteria(UsuarioPartida.class, "up")
                .createAlias("up.partida", "p") // join con partida
                .add(Restrictions.eq("up.usuario", u)) // partidas del usuario
                .add(Restrictions.in("p.estadoPartida", Arrays.asList(ESTADO_PARTIDA.ABIERTA, ESTADO_PARTIDA.EN_CURSO))) // estados abiertos o en curso
                .add(Restrictions.eq("p.tipo", TIPO_PARTIDA.MULTIJUGADOR)) // solo multijugador
                .setLockMode("p", LockMode.PESSIMISTIC_WRITE); // bloqueo si necesitás

        List<UsuarioPartida> usuarioPartidas = criteria.list();

        // extraemos las partidas para devolverlas
        List<Partida> partidas = usuarioPartidas.stream()
                .map(UsuarioPartida::getPartida)
                .collect(Collectors.toList());

        return partidas;
    }
}
