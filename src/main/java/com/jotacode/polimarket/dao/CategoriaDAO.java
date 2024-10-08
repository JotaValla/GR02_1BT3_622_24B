package com.jotacode.polimarket.dao;

import com.jotacode.polimarket.model.entity.Anuncio;
import com.jotacode.polimarket.model.entity.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class CategoriaDAO {
    private EntityManagerFactory factory;

    public CategoriaDAO(){
        factory = Persistence.createEntityManagerFactory("");
    }
    public void saveCategoria(Categoria categoria){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(categoria);
        em.getTransaction().commit();
        em.close();
    }
    //Obtener la lista de anuncios
    public List<Categoria> getCategoria() {
        EntityManager em = factory.createEntityManager();
        List<Categoria> categorias = em.createQuery("SELECT a FROM Categoria a", Categoria.class).getResultList();
        em.close();
        return categorias;
    }

    public Categoria getCategoriaById(Long id) {
        EntityManager em = factory.createEntityManager();
        Categoria categoria = em.find(Categoria.class, id);
        em.close();
        return categoria;
    }
    public void updateCategoria(Categoria categoria) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.merge(categoria);
        em.getTransaction().commit();
        em.close();
    }
    public void deleteCategoria(Long id) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Categoria categoria = em.find(Categoria.class, id);
        em.remove(categoria);
        em.getTransaction().commit();
        em.close();
    }


}
