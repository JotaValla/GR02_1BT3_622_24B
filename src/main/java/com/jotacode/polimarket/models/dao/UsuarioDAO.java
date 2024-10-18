package com.jotacode.polimarket.models.dao;

import com.jotacode.polimarket.models.entity.Usuario;
import jakarta.persistence.EntityManagerFactory;

public class UsuarioDAO extends AbstractDAO<Usuario> {

    public UsuarioDAO(EntityManagerFactory emf, Class<Usuario> entityClass) {
        super(emf, entityClass);
    }

    @Override
    protected Object getEntityId(Usuario entity) {
        return entity.getIdUsuario();
    }

}
