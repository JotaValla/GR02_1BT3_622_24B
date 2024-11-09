package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.services.AnuncioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/verAnuncioCompleto")
public class VerAnuncioCompletoServlet extends HttpServlet {

    private AnuncioService anuncioService;

    public VerAnuncioCompletoServlet() {
        this.anuncioService = new AnuncioService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String anuncioIdParam = request.getParameter("anuncioId");

        if (anuncioIdParam == null || anuncioIdParam.isEmpty()) {
            response.sendRedirect("verAnuncios");
            return;
        }

        try {
            Long anuncioId = Long.parseLong(anuncioIdParam);
            Anuncio anuncio = anuncioService.findById(anuncioId);

            if (anuncio == null) {
                response.sendRedirect("verAnuncios");
                return;
            }

            request.setAttribute("anuncio", anuncio);
            request.getRequestDispatcher("/WEB-INF/views/verAnuncioCompleto.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("verAnuncios");
        }
    }
}
