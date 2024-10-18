package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.dao.AnuncioDAO;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.services.AnuncioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/filtrarAnuncios")
public class FiltrarAnunciosServlet extends HttpServlet {

    private AnuncioService anuncioService;

    public FiltrarAnunciosServlet() {
        this.anuncioService = new AnuncioService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/filtrarAnuncios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoria = getCategoriaFromRequest(request);

        List<Anuncio> anunciosFiltrados = anuncioService.findAnunciosByCategoria(categoria);

        request.setAttribute("anuncios", anunciosFiltrados);
        request.getRequestDispatcher("/WEB-INF/views/verAnuncios.jsp").forward(request, response);
    }

    private String getCategoriaFromRequest(HttpServletRequest request) {
        String categoria = request.getParameter("categoria");
        if (categoria == null || categoria.trim().isEmpty()) {
            categoria = "todos";
        }
        return categoria;
    }

}
