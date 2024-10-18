package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.services.AnuncioService;
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

    public VerAnunciosServlet() {
        this.anuncioService = new AnuncioService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Anuncio> anuncios = anuncioService.findAllAnuncios();
        request.setAttribute("anuncios", anuncios);
        request.getRequestDispatcher("/WEB-INF/views/verAnuncios.jsp").forward(request, response);
    }
}