package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.models.entity.Valoracion;
import com.jotacode.polimarket.services.ValoracionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/editarValoracion")
public class EditarValoracionServlet extends HttpServlet {
    private ValoracionService valoracionService = new ValoracionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        Long valoracionId = Long.parseLong(request.getParameter("id"));
        Valoracion valoracion = valoracionService.findById(valoracionId);

        // Verificar si el usuario tiene permiso para editar la valoración
        if (usuario == null || !valoracion.getUsuValoracion().getIdUsuario().equals(usuario.getIdUsuario())) {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return;
        }
        request.setAttribute("valoracion", valoracion);
        request.getRequestDispatcher("/WEB-INF/views/editarValoracion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        Long valoracionId = Long.parseLong(request.getParameter("id"));
        Valoracion valoracion = valoracionService.findById(valoracionId);

        // Verificar si el usuario tiene permiso para modificar la valoración
        if (usuario == null || !valoracion.getUsuValoracion().getIdUsuario().equals(usuario.getIdUsuario())) {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return;
        }

        Integer estrellas = Integer.parseInt(request.getParameter("estrellas"));
        String comentario = request.getParameter("comentario");

        try {
            valoracionService.updateValoracion(valoracionId, estrellas, comentario);
            request.setAttribute("successMessage", "Valoración actualizada exitosamente.");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error al actualizar la valoración.");
        }

        // Recargar la valoración actualizada para mostrar en el formulario
        valoracion = valoracionService.findById(valoracionId);
        request.setAttribute("valoracion", valoracion);

        // Volver a la página de edición con el mensaje correspondiente
        request.getRequestDispatcher("/WEB-INF/views/editarValoracion.jsp").forward(request, response);
    }

}
