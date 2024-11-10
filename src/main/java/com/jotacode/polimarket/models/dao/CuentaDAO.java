package com.jotacode.polimarket.models.dao;

import com.jotacode.polimarket.models.entity.Cuenta;
import jakarta.persistence.EntityManagerFactory;

public class CuentaDAO extends  AbstractDAO<Cuenta> {

    public CuentaDAO(EntityManagerFactory emf, Class<Cuenta> entityClass) {
        super(emf, entityClass);
    }

    @Override
    protected Object getEntityId(Cuenta entity) {
        return entity.getIdCuenta();
    }

    public Cuenta findByUsernameAndPassword(String username, String password) {
        return (Cuenta) getEntityManager().createQuery("SELECT c FROM Cuenta c WHERE c.username = :username AND c.password = :password")
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
    }

    public boolean existsUsername(String username) {
        try {
            Long count = (Long) getEntityManager()
                .createQuery("SELECT COUNT(c) FROM Cuenta c WHERE c.username = :username")
                .setParameter("username", username)
                .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
