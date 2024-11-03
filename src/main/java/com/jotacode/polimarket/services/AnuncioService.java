package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.AnuncioDAO;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;

import java.math.BigDecimal;
import java.util.List;

public class AnuncioService {

    public AnuncioDAO anuncioDAO;

    public AnuncioService() {
        this.anuncioDAO = new AnuncioDAO(null, Anuncio.class);
    }

    public Anuncio crearAnuncio(String titulo, String descripcion, String imagen, String categoria, BigDecimal precio){
        Anuncio anuncio = new Anuncio();
        validarCamposAnuncio(titulo, descripcion, imagen, categoria, precio);
        anuncio.setTitulo(titulo);
        anuncio.setDescripcion(descripcion);
        anuncio.setImagen(imagen);
        anuncio.setCategoria(categoria);
        anuncio.setPrecio(precio);
        return anuncio;
    }

    private static void validarCamposAnuncio(String titulo, String descripcion, String imagen, String categoria, BigDecimal precio) {
        if (titulo == null || descripcion == null || categoria == null || precio == null) {
            throw new IllegalArgumentException("Todo los campos deben ser llenados");
        }
    }

    public void vincularAnuncioConUsuario(Anuncio anuncio, Usuario usuario) throws IllegalArgumentException {
        validarAnuncioyUsuario(anuncio, usuario);
        anuncio.setUsuAnuncio(usuario);
        anuncioDAO.create(anuncio);
    }

    private static void validarAnuncioyUsuario(Anuncio anuncio, Usuario usuario) {
        if (anuncio == null || usuario == null) {
            throw new IllegalArgumentException("El anuncio y el usuario no pueden ser nulos");
        }
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
