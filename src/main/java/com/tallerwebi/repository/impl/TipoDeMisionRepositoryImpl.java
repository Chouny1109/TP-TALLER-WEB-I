package com.tallerwebi.repository.impl;

import com.tallerwebi.dominio.enums.TIPO_MISION;
import com.tallerwebi.model.TipoDeMision;
import com.tallerwebi.repository.TipoDeMisionRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class TipoDeMisionRepositoryImpl implements TipoDeMisionRepository {

    private final SessionFactory sessionFactory;

    public TipoDeMisionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean existePorNombre(TIPO_MISION tipo) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();//Herramienta para construir consultas
        CriteriaQuery<Long> query = builder.createQuery(Long.class);//Que es lo que queremos obtener
        Root<TipoDeMision> root = query.from(TipoDeMision.class);//Sobre que entidad va a buscar y obtener los campos

        query.select(builder.count(root))// hace un select count(*) from TipoDeMision = root
                .where(builder.equal(root.get("nombre"), tipo));//filtramos la consulta

        Long count = sessionFactory.getCurrentSession().createQuery(query).getSingleResult();//ejecutamos la consulta

        return count != null && count > 0;
    }

    @Override
    public void save(TipoDeMision tipoMision) {
        sessionFactory.getCurrentSession().save(tipoMision);
    }
}
