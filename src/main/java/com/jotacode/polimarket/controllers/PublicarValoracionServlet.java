package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.model.dao.AnuncioDAO;
import com.jotacode.polimarket.model.dao.UsuarioDAO;
import com.jotacode.polimarket.model.dao.ValoracionDAO;
import com.jotacode.polimarket.model.entity.Anuncio;
import com.jotacode.polimarket.model.entity.Usuario;
import com.jotacode.polimarket.model.entity.Valoracion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/publicarValoracion")
public class PublicarValoracionServlet extends HttpServlet {
    private ValoracionDAO valoracionDAO;
    private AnuncioDAO anuncioDAO;
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        valoracionDAO = new ValoracionDAO(null, Valoracion.class);
        anuncioDAO = new AnuncioDAO(null, Anuncio.class);
        usuarioDAO = new UsuarioDAO(null, Usuario.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Anuncio> anuncios = anuncioDAO.findAll();
        request.setAttribute("anuncios", anuncios);
        request.getRequestDispatcher("/WEB-INF/views/publicarValoracion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer estrellas = Integer.parseInt(request.getParameter("estrellas"));
        String comentario = request.getParameter("comentario");
        Long anuncioId = Long.parseLong(request.getParameter("anuncioId"));

        Usuario usuario = createUserFromRequest(request);
        usuarioDAO.create(usuario);

        Anuncio anuncio = anuncioDAO.find(anuncioId);

        Valoracion valoracion = createValoracion(estrellas, comentario, anuncio, usuario);
        valoracionDAO.create(valoracion);

        response.sendRedirect(request.getContextPath() + "/verAnuncios");
    }

    private Usuario createUserFromRequest(HttpServletRequest request) {
        String username = request.getParameter("username");
        String foto = request.getParameter("foto");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setFoto(foto);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        return usuario;
    }

    private Valoracion createValoracion(Integer estrellas, String comentario, Anuncio anuncio, Usuario usuario) {
        Valoracion valoracion = new Valoracion();
        valoracion.setEstrellas(estrellas);
        valoracion.setComentario(comentario);
        valoracion.setAnun(anuncio);
        valoracion.setUsuValoracion(usuario);
        return valoracion;
    }

}