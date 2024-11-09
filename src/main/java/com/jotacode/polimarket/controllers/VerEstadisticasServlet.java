package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.services.AnuncioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/verEstadisticas")
public class VerEstadisticasServlet extends HttpServlet {

    private AnuncioService anuncioService;

    public VerEstadisticasServlet() {
        this.anuncioService = new AnuncioService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String anuncioIdParam = request.getParameter("anuncioId");

        if (anuncioIdParam == null || anuncioIdParam.isEmpty()) {
            response.sendRedirect("menu");
            return;
        }

        try {
            Long anuncioId = Long.parseLong(anuncioIdParam);
            Anuncio anuncio = anuncioService.findById(anuncioId);
            if (anuncio == null) {
                response.sendRedirect("menu");
                return;
            }

            // Obtener el promedio de valoraciones
            double promedioValoraciones = anuncioService.calcularPromedioValoraciones(anuncioId);

            request.setAttribute("anuncio", anuncio);
            request.setAttribute("promedioValoraciones", promedioValoraciones);
            request.getRequestDispatcher("/WEB-INF/views/verEstadisticas.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("menu");
        }
    }
}
