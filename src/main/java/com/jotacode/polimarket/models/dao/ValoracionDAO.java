package com.jotacode.polimarket.models.dao;


import com.jotacode.polimarket.models.entity.Valoracion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

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
    public List<Valoracion> findValoracionesByUsuario(Long usuarioId) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT v FROM Valoracion v WHERE v.usuValoracion.idUsuario = :usuarioId", Valoracion.class)
                    .setParameter("usuarioId", usuarioId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    public void updateValoracion(Valoracion valoracion) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(valoracion);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    public Valoracion findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Valoracion.class, id);
        } finally {
            em.close();
        }
    }
    public Valoracion findValoracionByUsuarioAndAnuncio(Long usuarioId, Long anuncioId) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT v FROM Valoracion v WHERE v.usuValoracion.idUsuario = :usuarioId AND v.anun.idAnuncio = :anuncioId", Valoracion.class)
                    .setParameter("usuarioId", usuarioId)
                    .setParameter("anuncioId", anuncioId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // No hay valoración existente
        } finally {
            em.close();
        }
    }


}
