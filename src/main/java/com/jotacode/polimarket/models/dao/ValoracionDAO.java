package com.jotacode.polimarket.models.dao;


import com.jotacode.polimarket.models.entity.Valoracion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ValoracionDAO extends AbstractDAO<Valoracion> {

    public ValoracionDAO(EntityManagerFactory emf, Class<Valoracion> entityClass) {
        super(emf, entityClass);
    }

    @Override
    protected Object getEntityId(Valoracion entity) {
        return entity.getIdValoracion();
    }


    public List<Valoracion> findValoracionesByAnuncio(Long anuncioId) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT v FROM Valoracion v WHERE v.anun.id = :anuncioId", Valoracion.class)
                    .setParameter("anuncioId", anuncioId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
