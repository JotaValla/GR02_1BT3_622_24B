package com.jotacode.polimarket.controllers;

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
import java.util.List;

@WebServlet("/verAnuncios")
public class VerAnunciosServlet extends HttpServlet {

    private AnuncioService anuncioService;
    private UsuarioService usuarioService;

    public VerAnunciosServlet() {
        this.anuncioService = new AnuncioService();
        this.usuarioService = new UsuarioService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usuarioId = request.getParameter("usuarioId");
        List<Anuncio> anuncios;

        if (usuarioId != null && !usuarioId.isEmpty()) {
            anuncios = anuncioService.findAnunciosByUsuario(Long.parseLong(usuarioId));
        } else {
            anuncios = anuncioService.findAllAnuncios();
        }

        List<Usuario> usuarios = usuarioService.findAllUsuarios();

        request.setAttribute("anuncios", anuncios);
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/WEB-INF/views/verAnuncios.jsp").forward(request, response);
    }
}