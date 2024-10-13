package com.jotacode.polimarket.model.dao;


import com.jotacode.polimarket.model.dao.exceptions.NonexistentEntityException;
import com.jotacode.polimarket.model.entity.Anuncio;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;


import java.io.Serializable;
import java.util.List;

public class AnuncioDAO implements Serializable {

    private EntityManagerFactory emf = null;

    public AnuncioDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public AnuncioDAO() {
        this.emf = Persistence.createEntityManagerFactory("PolimarketPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Anuncio anuncio) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(anuncio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Anuncio anuncio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            anuncio = em.merge(anuncio);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = anuncio.getIdAnuncio();
                if (findAnuncio(id) == null) {
                    throw new NonexistentEntityException("El anuncio con id:  " + id + " no existe.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Anuncio anuncio;
            try {
                anuncio = em.getReference(Anuncio.class, id);
                anuncio.getIdAnuncio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El anuncio con id:  " + id + " no existe.", enfe);
            }
            em.remove(anuncio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
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

    public List<Anuncio> findAnuncioEntities() {
        return findAnuncioEntities(true, -1, -1);
    }

    public List<Anuncio> findAnuncioEntities(int maxResults, int firstResult) {
        return findAnuncioEntities(false, maxResults, firstResult);
    }

    private List<Anuncio> findAnuncioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Anuncio.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Anuncio findAnuncio(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Anuncio.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnuncioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Anuncio> rt = cq.from(Anuncio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
