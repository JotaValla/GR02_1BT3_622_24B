package com.jotacode.polimarket.model.dao;


import com.jotacode.polimarket.model.entity.Anuncio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


import java.util.List;

public class AnuncioDAO {

    private EntityManagerFactory emf;



    public AnuncioDAO() {
        this.emf = Persistence.createEntityManagerFactory("PolimarketPU");
    }

    //Guardar un anuncio
    public void saveAnuncio(Anuncio anuncio) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(anuncio);
        em.getTransaction().commit();
        em.close();
    }

    //Obtener la lista de anuncios
    public List<Anuncio> getAnuncios() {
        EntityManager em = emf.createEntityManager();
        List<Anuncio> anuncios = null;
        try {
            em.getTransaction().begin();
            anuncios = em.createQuery("FROM Anuncio a JOIN FETCH a.usuario", Anuncio.class).getResultList();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return anuncios;
    }

    //Obtener un anuncio por su id
    public Anuncio getAnuncioById(Long id) {
        EntityManager em = emf.createEntityManager();
        Anuncio anuncio = em.find(Anuncio.class, id);
        em.close();
        return anuncio;
    }

    //Actualizar un anuncio
    public void updateAnuncio(Anuncio anuncio) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(anuncio);
        em.getTransaction().commit();
        em.close();
    }

    //Eliminar un anuncio
    public void deleteAnuncio(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Anuncio anuncio = em.find(Anuncio.class, id);
        em.remove(anuncio);
        em.getTransaction().commit();
        em.close();
    }



}
