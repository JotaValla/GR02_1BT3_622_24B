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

@WebServlet("/verAnuncios")
public class VerAnunciosServlet extends HttpServlet {
    private AnuncioDAO anuncioDAO;

    @Override
    public void init() {
        anuncioDAO = new AnuncioDAO(null, Anuncio.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Anuncio> anuncios = anuncioDAO.findAll();
        request.setAttribute("anuncios", anuncios);
        request.getRequestDispatcher("/WEB-INF/views/verAnuncios.jsp").forward(request, response);
    }
}