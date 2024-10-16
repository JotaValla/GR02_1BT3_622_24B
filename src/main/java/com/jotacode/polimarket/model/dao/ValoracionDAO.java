package com.jotacode.polimarket.model.dao;


import com.jotacode.polimarket.model.entity.Usuario;
import com.jotacode.polimarket.model.entity.Valoracion;
import jakarta.persistence.EntityManagerFactory;

public class ValoracionDAO extends AbstractDAO<Valoracion>  {

    public ValoracionDAO(EntityManagerFactory emf, Class<Valoracion> entityClass) {
        super(emf, entityClass);
    }

    @Override
    protected Object getEntityId(Valoracion entity) {
        return entity.getIdValoracion();
    }


}
