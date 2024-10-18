package com.jotacode.polimarket.services;


import com.jotacode.polimarket.models.dao.ValoracionDAO;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.models.entity.Valoracion;

public class ValoracionService {

    private ValoracionDAO valoracionDAO;

    public ValoracionService() {
        this.valoracionDAO = new ValoracionDAO(null, Valoracion.class);
    }

    public Valoracion crearValoracion(Integer estrellas, String comentario) {
        Valoracion valoracion = new Valoracion();
        valoracion.setEstrellas(estrellas);
        valoracion.setComentario(comentario);
        return valoracion;
    }

    public void vincularValoracion(Valoracion valoracion, Anuncio anuncio, Usuario usuario) {
        valoracion.setAnun(anuncio);
        valoracion.setUsuValoracion(usuario);
        valoracionDAO.create(valoracion);
    }


}
