package com.jotacode.polimarket.models.dao;

import com.jotacode.polimarket.models.entity.Anuncio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class AnuncioDAO extends AbstractDAO<Anuncio> {

    public AnuncioDAO(EntityManagerFactory emf, Class<Anuncio> entityClass) {
        super(emf, entityClass);
    }

    @Override
    protected Object getEntityId(Anuncio entity) {
        return entity.getIdAnuncio();
    }

    public List<Anuncio> findAnunciosByCategoria(String categoria) {
        EntityManager em = getEntityManager();
        try {
            if (categoria.equalsIgnoreCase("todos")) {
                return em.createQuery("SELECT a FROM Anuncio a", Anuncio.class).getResultList();
            } else {
                return em.createQuery("SELECT a FROM Anuncio a WHERE LOWER(a.categoria) = LOWER(:categoria)", Anuncio.class)
                        .setParameter("categoria", categoria)
                        .getResultList();
            }
        } finally {
            em.close();
        }
    }

}
