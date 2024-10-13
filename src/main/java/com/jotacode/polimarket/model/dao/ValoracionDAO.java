package com.jotacode.polimarket.model.dao;

import com.jotacode.polimarket.model.dao.exceptions.NonexistentEntityException;
import com.jotacode.polimarket.model.entity.Valoracion;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;
import java.util.List;

public class ValoracionDAO implements Serializable {

    private EntityManagerFactory emf = null;

    public ValoracionDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public ValoracionDAO() {
        this.emf = Persistence.createEntityManagerFactory("PolimarketPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Valoracion valoracion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(valoracion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Valoracion valoracion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            valoracion = em.merge(valoracion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = valoracion.getIdValoracion();
                if (findValoracion(id) == null) {
                    throw new NonexistentEntityException("El valoracion con id:  " + id + " no existe.");
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
            Valoracion valoracion;
            try {
                valoracion = em.getReference(Valoracion.class, id);
                valoracion.getIdValoracion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El valoracion con id:  " + id + " no existe.", enfe);
            }
            em.remove(valoracion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Valoracion> findValoracion() {
        return findValoracionEntities(true, -1, -1);
    }

    public List<Valoracion> findValoracion(int maxResults, int firstResult) {
        return findValoracionEntities(false, maxResults, firstResult);
    }

    private List<Valoracion> findValoracionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Valoracion.class));
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

    public Valoracion findValoracion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Valoracion.class, id);
        } finally {
            em.close();
        }
    }

    public int getValoracion() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Valoracion> rt = cq.from(Valoracion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }


}
