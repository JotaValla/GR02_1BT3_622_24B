package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.dao.exceptions.NonexistentEntityException;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
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
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario"); // Usuario que está viendo el anuncio

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

            if (usuario != null && !usuario.getIdUsuario().equals(anuncio.getUsuAnuncio().getIdUsuario())) {
                anuncio.setVistas(anuncio.getVistas() + 1);
                anuncioService.actualizarAnuncio(anuncio); // Llamada a actualizarAnuncio para guardar las vistas incrementadas
            }

            request.setAttribute("anuncio", anuncio);
            request.getRequestDispatcher("/WEB-INF/views/verAnuncioCompleto.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("verAnuncios");
        } catch (NonexistentEntityException e) {
            throw new RuntimeException(e);
        }
    }

}
