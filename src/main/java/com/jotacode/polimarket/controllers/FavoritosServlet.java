package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.services.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Hibernate;

import java.io.IOException;
import java.util.List;

@WebServlet("/favoritos")
public class FavoritosServlet extends HttpServlet {
    private UsuarioService usuarioService;

    @Override
    public void init() {
        this.usuarioService = new UsuarioService(); // Instancia de UsuarioService
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Obtiene el usuario con la colección favoritos ya cargada
        Usuario usuarioConFavoritos = usuarioService.findById(usuario.getIdUsuario());

        List<Anuncio> favoritos = usuarioConFavoritos.getFavoritos();
        request.setAttribute("favoritos", favoritos);
        request.getRequestDispatcher("/WEB-INF/views/verFavoritos.jsp").forward(request, response);
    }
}
