package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.model.dao.AnuncioDAO;
import com.jotacode.polimarket.model.entity.Anuncio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/filtrarAnuncios")
public class FiltrarAnunciosServlet extends HttpServlet {
    private AnuncioDAO anuncioDAO;

    @Override
    public void init() {
        anuncioDAO = new AnuncioDAO(null, Anuncio.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/filtrarAnuncios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoria = request.getParameter("categoria");
        if (categoria == null || categoria.trim().isEmpty()) {
            categoria = "todos"; // Valor por defecto si no se proporciona ninguna categoría
        }

        List<Anuncio> anunciosFiltrados = anuncioDAO.findAnunciosByCategoria(categoria);

        request.setAttribute("anuncios", anunciosFiltrados);
        request.getRequestDispatcher("/WEB-INF/views/verAnuncios.jsp").forward(request, response);
    }
}
