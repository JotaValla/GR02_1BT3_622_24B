package com.jotacode.polimarket.models.dao;


import com.jotacode.polimarket.models.entity.Valoracion;
import jakarta.persistence.EntityManagerFactory;

public class ValoracionDAO extends AbstractDAO<Valoracion> {

    public ValoracionDAO(EntityManagerFactory emf, Class<Valoracion> entityClass) {
        super(emf, entityClass);
    }

    @Override
    protected Object getEntityId(Valoracion entity) {
        return entity.getIdValoracion();
    }


}
