package com.jotacode.polimarket.model.dao;

import com.jotacode.polimarket.model.dao.exceptions.NonexistentEntityException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;

public abstract class AbstractDAO<T> implements GenericDAO<T> {

    private EntityManagerFactory emf;
    private Class<T> entityClass;

    public AbstractDAO(EntityManagerFactory emf, Class<T> entityClass) {
        this.emf = emf != null ? emf : Persistence.createEntityManagerFactory("PolimarketPU");
        this.entityClass = entityClass;
    }

    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void create(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(T entity) throws NonexistentEntityException, Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            if (em.find(entityClass, getEntityId(entity)) == null) {
                throw new NonexistentEntityException("La entidad con id no existe.");
            }
            em.merge(entity);
            em.getTransaction().commit();
        } catch (EntityNotFoundException ex) {
            throw new NonexistentEntityException("Error al editar: la entidad no existe.", ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T entity = em.getReference(entityClass, id);
            try {
                em.remove(entity);
            } catch (EntityNotFoundException ex) {
                throw new NonexistentEntityException("La entidad con id: " + id + " no existe.", ex);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public T find(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<T> findAll() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<T> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<T> findEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(entityClass);
            cq.select(cq.from(entityClass));
            var query = em.createQuery(cq);
            if (!all) {
                query.setMaxResults(maxResults);
                query.setFirstResult(firstResult);
            }
            return query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public int getCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Long> cq = em.getCriteriaBuilder().createQuery(Long.class);
            cq.select(em.getCriteriaBuilder().count(cq.from(entityClass)));
            return em.createQuery(cq).getSingleResult().intValue();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // Método abstracto para obtener el ID de la entidad
    protected abstract Object getEntityId(T entity);
}
