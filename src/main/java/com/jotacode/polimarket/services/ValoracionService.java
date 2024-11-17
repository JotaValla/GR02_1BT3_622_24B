package com.jotacode.polimarket.services;


import com.jotacode.polimarket.models.dao.ValoracionDAO;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.models.entity.Valoracion;

import java.util.List;

public class ValoracionService {

    public ValoracionDAO valoracionDAO;

    public ValoracionService() {
        this.valoracionDAO = new ValoracionDAO(null, Valoracion.class);
    }

    public Valoracion crearValoracion(Integer estrellas, String comentario) {
        validarParametrosValoracion(estrellas, comentario);
        Valoracion valoracion = new Valoracion();
        valoracion.setEstrellas(estrellas);
        valoracion.setComentario(comentario);
        return valoracion;
    }

    private void validarParametrosValoracion(Integer estrellas, String comentario) {
        if (estrellas == null || comentario == null) {
            throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
        }
        if (estrellas < 1 || estrellas > 5) {  // Validación de rango
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
        }
    }

    public void vincularValoracion(Valoracion valoracion, Anuncio anuncio, Usuario usuario) {
        validarAnuncioUsuarioValoracion(anuncio, usuario, valoracion);
        valoracion.setAnun(anuncio);
        valoracion.setUsuValoracion(usuario);
        valoracionDAO.create(valoracion);
    }

    private void validarAnuncioUsuarioValoracion(Anuncio anuncio, Usuario usuario, Valoracion valoracion) {
        if (anuncio == null || usuario == null || valoracion == null) {
            throw new IllegalArgumentException("El anuncio, usuario o valoracion no pueden ser nulos");
        }
    }
    // Método para actualizar una valoración
    public void updateValoracion(Long valoracionId, Integer estrellas, String comentario) {
        Valoracion valoracion = findById(valoracionId);
        if (valoracion == null) {
            throw new IllegalArgumentException("La valoración no existe");
        }
        // Actualiza los campos de la valoración
        valoracion.setEstrellas(estrellas);
        valoracion.setComentario(comentario);
        // Llama al DAO para guardar los cambios
        valoracionDAO.updateValoracion(valoracion);
    }

    public Valoracion findById(Long id) {
        return valoracionDAO.findById(id);
    }

    public List<Valoracion> findValoracionesByUsuario(Long usuarioId) {
        return valoracionDAO.findValoracionesByUsuario(usuarioId);
    }

    public List<Valoracion> findValoracionesByAnuncio(Long anuncioId) {
        return valoracionDAO.findValoracionesByAnuncio(anuncioId);
    }

    public boolean existeValoracionDeUsuarioParaAnuncio(Long usuarioId, Long anuncioId) {
        return valoracionDAO.findValoracionByUsuarioAndAnuncio(usuarioId, anuncioId) != null;
    }
}
