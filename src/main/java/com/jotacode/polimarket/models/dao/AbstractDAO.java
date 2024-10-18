package com.jotacode.polimarket.models.dao;

import com.jotacode.polimarket.models.dao.exceptions.NonexistentEntityException;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractDAO<T> implements GenericDAO<T> {

    private EntityManagerFactory emf;
    private Class<T> entityClass;

    public AbstractDAO(EntityManagerFactory emf, Class<T> entityClass) {
        this.emf = getEntityManagerFactory(emf);
        this.entityClass = entityClass;
    }

    private EntityManagerFactory getEntityManagerFactory(EntityManagerFactory emf) {
        return emf != null ? emf : Persistence.createEntityManagerFactory("PolimarketPU");
    }

    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    protected void executeInTransaction(Consumer<EntityManager> action) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            action.accept(em);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void create(T entity) {
        executeInTransaction(em -> em.persist(entity));
    }

    @Override
    public void edit(T entity) throws NonexistentEntityException {
        executeInTransaction(em -> {
            try {
                if (em.find(entityClass, getEntityId(entity)) == null) {
                    throw new NonexistentEntityException("La entidad con id no existe.");
                }
                em.merge(entity);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        });
    }

    @Override
    public void destroy(Long id) throws NonexistentEntityException {
        executeInTransaction(em -> {
            T entity = em.getReference(entityClass, id);
            em.remove(entity);
        });
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
            TypedQuery<T> query = em.createQuery(cq);

            boolean hasLimits = !all && maxResults >= 0 && firstResult >= 0;
            if (hasLimits) {
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

    protected abstract Object getEntityId(T entity);
}
