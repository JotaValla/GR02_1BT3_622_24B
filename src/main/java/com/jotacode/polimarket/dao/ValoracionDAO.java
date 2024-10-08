package com.jotacode.polimarket.dao;

import com.jotacode.polimarket.model.entity.Valoracion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class ValoracionDAO {

    private EntityManagerFactory emf;

    public ValoracionDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public ValoracionDAO() {
        this.emf = Persistence.createEntityManagerFactory("PolimarketPU");
    }

    //Guardar valoracion
    public void saveValoracion(Valoracion valoracion){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(valoracion);
        em.getTransaction().commit();
        em.close();
    }

    //Obtener una lista de valoraciones
    public List<Valoracion> getValoraciones() {
        EntityManager em = emf.createEntityManager();
        List<Valoracion> valoraciones = em.createQuery("SELECT v FROM Valoracion v", Valoracion.class).getResultList();
        em.close();
        return valoraciones;
    }

    //Obtener una valoracion por su id
    public Valoracion getValoracionById(Long id) {
        EntityManager em = emf.createEntityManager();
        Valoracion valoracion = em.find(Valoracion.class, id);
        em.close();
        return valoracion;
    }

    //Actualizar una valoracion
    public void updateValoracion(Valoracion valoracion) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(valoracion);
        em.getTransaction().commit();
        em.close();
    }

    //Eliminar una valoracion
    public void deleteValoracion(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Valoracion valoracion = em.find(Valoracion.class, id);
        em.remove(valoracion);
        em.getTransaction().commit();
        em.close();
    }

}
