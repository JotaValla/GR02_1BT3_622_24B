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
import java.util.List;

@WebServlet("/misValoraciones")
public class MisValoracionesServlet extends HttpServlet {
    private ValoracionService valoracionService = new ValoracionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Recuperar las valoraciones del usuario logueado
        List<Valoracion> valoraciones = valoracionService.findValoracionesByUsuario(usuario.getIdUsuario());
        request.setAttribute("valoraciones", valoraciones);

        request.getRequestDispatcher("/WEB-INF/views/misValoraciones.jsp").forward(request, response);
    }
}
