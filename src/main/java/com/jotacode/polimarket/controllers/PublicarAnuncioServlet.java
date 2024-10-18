package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.dao.AnuncioDAO;
import com.jotacode.polimarket.models.dao.UsuarioDAO;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.services.AnuncioService;
import com.jotacode.polimarket.services.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/publicarAnuncio")
public class PublicarAnuncioServlet extends HttpServlet {

    private UsuarioService usuarioService;
    private AnuncioService anuncioService;

    public PublicarAnuncioServlet() {
        this.usuarioService = new UsuarioService();
        this.anuncioService = new AnuncioService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/publicarAnuncio.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Usuario usuario = createUserFromRequest(request);
        Anuncio anuncio = createAnuncioFromRequest(request);

        usuarioService.publicarAnuncio(anuncio, usuario);

        response.sendRedirect(request.getContextPath() + "/verAnuncios");
    }

    private Usuario createUserFromRequest(HttpServletRequest request) {
        String username = request.getParameter("username");
        String foto = request.getParameter("foto");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        return usuarioService.crearUsuario(username, foto, telefono, email);
    }

    private Anuncio createAnuncioFromRequest(HttpServletRequest request) {
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String imagen = request.getParameter("imagen");
        String categoria = request.getParameter("categoria");
        BigDecimal precio = new BigDecimal(request.getParameter("precio"));
        return anuncioService.crearAnuncio(titulo, descripcion, imagen, categoria, precio);
    }

}