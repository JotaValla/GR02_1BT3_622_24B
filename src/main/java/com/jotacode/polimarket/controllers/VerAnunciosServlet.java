package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.model.dao.AnuncioDAO;
import com.jotacode.polimarket.model.entity.Anuncio;
import com.jotacode.polimarket.model.entity.Valoracion;
import jakarta.servlet.RequestDispatcher;
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
    public void init() throws ServletException {
        anuncioDAO = new AnuncioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AnuncioDAO anuncioDAO = new AnuncioDAO();
        List<Anuncio> anuncios = anuncioDAO.getAnuncios();

        // Añadir la lista de anuncios al contexto de la solicitud
        request.setAttribute("anuncios", anuncios);

        // Redirigir a la JSP que muestra los anuncios
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/verAnuncios.jsp");
        dispatcher.forward(request, response);
    }
}

