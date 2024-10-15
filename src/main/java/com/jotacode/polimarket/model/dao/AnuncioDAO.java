package com.jotacode.polimarket.model.dao;

import com.jotacode.polimarket.model.entity.Anuncio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AnuncioDAO extends AbstractDAO<Anuncio> {

    public AnuncioDAO(EntityManagerFactory emf, Class<Anuncio> entityClass) {
        super(emf, entityClass);
    }

    public List<Anuncio> findAnunciosByCategoria(String categoria) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Anuncio> query;
            if (categoria.equalsIgnoreCase("todos")) {
                // Si la categoría es "todos", retornar todos los anuncios
                query = em.createQuery("SELECT a FROM Anuncio a", Anuncio.class);
            } else {
                // Filtrar por categoría
                query = em.createQuery("SELECT a FROM Anuncio a WHERE LOWER(a.categoria) = LOWER(:categoria)", Anuncio.class);
                query.setParameter("categoria", categoria);
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }

}
