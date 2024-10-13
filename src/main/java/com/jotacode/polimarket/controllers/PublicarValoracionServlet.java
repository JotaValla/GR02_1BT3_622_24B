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
        valoracionDAO = new ValoracionDAO();
        anuncioDAO = new AnuncioDAO();
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Anuncio> anuncios = anuncioDAO.findAnuncioEntities();
        request.setAttribute("anuncios", anuncios);
        request.getRequestDispatcher("/WEB-INF/views/publicarValoracion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer estrellas = Integer.parseInt(request.getParameter("estrellas"));
        String comentario = request.getParameter("comentario");
        Long anuncioId = Long.parseLong(request.getParameter("anuncioId"));

        // Usuario information
        String username = request.getParameter("username");
        String foto = request.getParameter("foto");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setFoto(foto);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        usuarioDAO.create(usuario);

        Anuncio anuncio = anuncioDAO.findAnuncio(anuncioId);

        Valoracion valoracion = new Valoracion();
        valoracion.setEstrellas(estrellas);
        valoracion.setComentario(comentario);
        valoracion.setAnun(anuncio);
        valoracion.setUsuValoracion(usuario);

        valoracionDAO.create(valoracion);

        response.sendRedirect(request.getContextPath() + "/verAnuncios");
    }
}