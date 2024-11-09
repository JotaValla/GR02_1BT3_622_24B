package com.jotacode.polimarket.services;


import com.jotacode.polimarket.models.dao.ValoracionDAO;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.models.entity.Valoracion;

import java.util.List;

public class ValoracionService {

    private ValoracionDAO valoracionDAO;

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
        if (estrellas > 5) {
            throw new IllegalArgumentException("La calificación no puede ser mayor a 5");
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

    public List<Valoracion> findValoracionesByAnuncio(Long anuncioId) {
        return valoracionDAO.findValoracionesByAnuncio(anuncioId);
    }

    public boolean existeValoracionDeUsuarioParaAnuncio(Long usuarioId, Long anuncioId) {
        return valoracionDAO.findValoracionByUsuarioAndAnuncio(usuarioId, anuncioId) != null;
    }

}
