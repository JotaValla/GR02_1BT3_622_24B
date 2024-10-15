package com.jotacode.polimarket.model.dao;

import com.jotacode.polimarket.model.entity.Usuario;
import jakarta.persistence.EntityManagerFactory;

public class UsuarioDAO extends AbstractDAO<Usuario> {

    public UsuarioDAO(EntityManagerFactory emf, Class<Usuario> entityClass) {
        super(emf, entityClass);
    }

}
