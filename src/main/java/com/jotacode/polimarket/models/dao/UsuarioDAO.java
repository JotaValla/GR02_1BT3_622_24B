package com.jotacode.polimarket.models.dao;

import com.jotacode.polimarket.models.entity.Cuenta;
import com.jotacode.polimarket.models.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

public class UsuarioDAO extends AbstractDAO<Usuario> {

    public UsuarioDAO(EntityManagerFactory emf, Class<Usuario> entityClass) {
        super(emf, entityClass);
    }

    @Override
    protected Object getEntityId(Usuario entity) {
        return entity.getIdUsuario();
    }

    public Usuario findUserByUsername(String username) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE LOWER(u.username) = :username", Usuario.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public Usuario findByCuenta(Cuenta cuenta) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.cuenta = :cuenta", Usuario.class)
                    .setParameter("cuenta", cuenta)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Retornar null si no se encuentra el resultado
            return null;
        } finally {
            em.close();
        }
    }

    public Usuario findByIdWithFavoritos(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u LEFT JOIN FETCH u.favoritos WHERE u.idUsuario = :id", Usuario.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Usuario findByIdWithAnuncios(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u LEFT JOIN FETCH u.anuncios WHERE u.idUsuario = :id", Usuario.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}
