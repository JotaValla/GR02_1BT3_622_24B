package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.AnuncioDAO;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;

import java.math.BigDecimal;
import java.util.List;

public class AnuncioService {

    private AnuncioDAO anuncioDAO;

    public AnuncioService() {
        this.anuncioDAO = new AnuncioDAO(null, Anuncio.class);
    }

    public Anuncio crearAnuncio(String titulo, String descripcion, String imagen, String categoria, BigDecimal precio) {
        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo(titulo);
        anuncio.setDescripcion(descripcion);
        anuncio.setImagen(imagen);
        anuncio.setCategoria(categoria);
        anuncio.setPrecio(precio);
        return anuncio;
    }

    public void vincularAnuncioConUsuario(Anuncio anuncio, Usuario usuario) {
        anuncio.setUsuAnuncio(usuario);
        anuncioDAO.create(anuncio);
    }

    public List<Anuncio> findAllAnuncios() {
        return anuncioDAO.findAll();
    }

    public List<Anuncio> findAnunciosByCategoria(String categoria) {
        return anuncioDAO.findAnunciosByCategoria(categoria);
    }

    public Anuncio findById(Long id) {
        return anuncioDAO.find(id);
    }

    public List<Anuncio> findAnunciosByUsuario(long idUsuario) {
        return anuncioDAO.findAnunciosByUsuario(idUsuario);
    }
}
