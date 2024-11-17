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
        String usuarioIdParam = request.getParameter("usuarioId");
        String categoria = request.getParameter("categoria");

        List<Anuncio> anuncios;

        if ((usuarioIdParam != null && !usuarioIdParam.isEmpty()) && (categoria != null && !categoria.isEmpty())) {
            // Filtrar por usuario y categoría
            Long usuarioId = Long.parseLong(usuarioIdParam);
            anuncios = anuncioService.findAnunciosByUsuarioAndCategoria(usuarioId, categoria);
        } else if (usuarioIdParam != null && !usuarioIdParam.isEmpty()) {
            // Filtrar solo por usuario
            Long usuarioId = Long.parseLong(usuarioIdParam);
            anuncios = anuncioService.findAnunciosByUsuario(usuarioId);
        } else if (categoria != null && !categoria.isEmpty()) {
            // Filtrar solo por categoría
            anuncios = anuncioService.findAnunciosByCategoria(categoria);
        } else {
            // Si no hay filtros, mostrar todos los anuncios
            anuncios = usuarioService.verAnuncios();
        }

        List<Usuario> usuarios = usuarioService.findAllUsuarios();

        request.setAttribute("anuncios", anuncios);
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/WEB-INF/views/verAnuncios.jsp").forward(request, response);
    }
}
