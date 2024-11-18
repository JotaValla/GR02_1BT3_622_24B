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
    private static final String MENU_PAGE = "menu";
    private static final String ESTADISTICAS_VIEW = "/WEB-INF/views/verEstadisticas.jsp";
    private final AnuncioService anuncioService;

    public VerEstadisticasServlet() {
        this.anuncioService = new AnuncioService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long anuncioId = obtenerAnuncioId(request, response);
        if (anuncioId == null) return;

        Anuncio anuncio = obtenerAnuncio(anuncioId, response);
        if (anuncio == null) return;

        double promedioValoraciones = anuncioService.calcularPromedioValoraciones(anuncioId);

        request.setAttribute("anuncio", anuncio);
        request.setAttribute("promedioValoraciones", promedioValoraciones);
        request.getRequestDispatcher(ESTADISTICAS_VIEW).forward(request, response);
    }

    private Long obtenerAnuncioId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String anuncioIdParam = request.getParameter("anuncioId");
        if (anuncioIdParam == null || anuncioIdParam.isEmpty()) {
            response.sendRedirect(MENU_PAGE);
            return null;
        }

        try {
            return Long.parseLong(anuncioIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(MENU_PAGE);
            return null;
        }
    }

    private Anuncio obtenerAnuncio(Long anuncioId, HttpServletResponse response) throws IOException {
        Anuncio anuncio = anuncioService.findById(anuncioId);
        if (anuncio == null) {
            response.sendRedirect(MENU_PAGE);
        }
        return anuncio;
    }
}
