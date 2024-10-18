package com.jotacode.polimarket.models.dao;

import com.jotacode.polimarket.models.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

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

}
