package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.model.dao.AnuncioDAO;
import com.jotacode.polimarket.model.entity.Anuncio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/filtrarAnuncios")
public class FiltrarAnuncioServlet extends HttpServlet {

    private AnuncioDAO anuncioDAO;

    @Override
    public void init() throws ServletException {
        anuncioDAO = new AnuncioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Mostrar el formulario de filtrado
        request.getRequestDispatcher("/WEB-INF/views/filtrarAnuncios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String titulo = request.getParameter("titulo");
        String precioMinStr = request.getParameter("precioMin");
        String precioMaxStr = request.getParameter("precioMax");

        BigDecimal precioMin = null;
        BigDecimal precioMax = null;

        try {
            if (precioMinStr != null && !precioMinStr.isEmpty()) {
                precioMin = new BigDecimal(precioMinStr);
            }
            if (precioMaxStr != null && !precioMaxStr.isEmpty()) {
                precioMax = new BigDecimal(precioMaxStr);
            }
        } catch (NumberFormatException e) {
            // Manejar errores de formato de precio
            request.setAttribute("error", "Rango de precios inválido.");
            request.getRequestDispatcher("/WEB-INF/views/filtrarAnuncios.jsp").forward(request, response);
            return;
        }

        // Obtener todos los anuncios y aplicar filtros
        List<Anuncio> anuncios = anuncioDAO.getAnuncios();

        if (titulo != null && !titulo.trim().isEmpty()) {
            anuncios = anuncios.stream()
                    .filter(a -> a.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (precioMin != null) {
            BigDecimal finalPrecioMin = precioMin;
            anuncios = anuncios.stream()
                    .filter(a -> a.getPrecio().compareTo(finalPrecioMin) >= 0)
                    .collect(Collectors.toList());
        }

        if (precioMax != null) {
            BigDecimal finalPrecioMax = precioMax;
            anuncios = anuncios.stream()
                    .filter(a -> a.getPrecio().compareTo(finalPrecioMax) <= 0)
                    .collect(Collectors.toList());
        }

        // Pasar los anuncios filtrados al JSP
        request.setAttribute("anuncios", anuncios);
        request.getRequestDispatcher("/WEB-INF/views/verAnuncios.jsp").forward(request, response);
    }
}
