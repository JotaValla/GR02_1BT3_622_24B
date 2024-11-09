package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.services.AnuncioService;
import com.jotacode.polimarket.services.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/guardarAnuncio")
public class GuardarAnuncioServlet extends HttpServlet {

    private UsuarioService usuarioService;
    private AnuncioService anuncioService;

    public GuardarAnuncioServlet() {
        this.usuarioService = new UsuarioService();
        this.anuncioService = new AnuncioService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtiene el usuario de la sesión
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Obtiene el anuncioId del formulario
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

            // Agrega el anuncio a los favoritos del usuario
            usuarioService.agregarFavorito(usuario, anuncio);

            // Recarga el usuario actualizado desde la base de datos para obtener los cambios en favoritos
            Usuario usuarioActualizado = usuarioService.findById(usuario.getIdUsuario());
            request.getSession().setAttribute("usuario", usuarioActualizado);

            // Redirige nuevamente a la página de detalles del anuncio
            response.sendRedirect("verAnuncioCompleto?anuncioId=" + anuncioId);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("verAnuncios");
        }
    }
}

