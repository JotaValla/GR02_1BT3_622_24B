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

    private static final String LOGIN_PAGE = "login.jsp";
    private static final String VIEW_ANUNCIOS = "verAnuncios";
    private static final String VIEW_ANUNCIO_COMPLETO = "verAnuncioCompleto";

    private final UsuarioService usuarioService;
    private final AnuncioService anuncioService;

    public GuardarAnuncioServlet() {
        this.usuarioService = new UsuarioService();
        this.anuncioService = new AnuncioService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Usuario usuario = obtenerUsuarioDeSesion(request);
            Long anuncioId = obtenerAnuncioId(request);

            // Agregar anuncio a favoritos y manejar la sesión
            agregarAnuncioAFavoritos(usuario, anuncioId);
            actualizarUsuarioEnSesion(request, usuario.getIdUsuario());

            // Redirigir con éxito
            response.sendRedirect(String.format("%s?anuncioId=%d&success=true", VIEW_ANUNCIO_COMPLETO, anuncioId));
        } catch (IllegalArgumentException e) {
            response.sendRedirect(VIEW_ANUNCIOS);
        } catch (Exception e) {
            throw new RuntimeException("Error procesando la solicitud", e);
        }
    }

    private Usuario obtenerUsuarioDeSesion(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no autenticado");
        }
        return usuario;
    }

    private Long obtenerAnuncioId(HttpServletRequest request) {
        String anuncioIdParam = request.getParameter("anuncioId");
        if (anuncioIdParam == null || anuncioIdParam.isEmpty()) {
            throw new IllegalArgumentException("ID de anuncio inválido");
        }
        try {
            return Long.parseLong(anuncioIdParam);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID de anuncio inválido");
        }
    }

    private void agregarAnuncioAFavoritos(Usuario usuario, Long anuncioId) throws Exception {
        Anuncio anuncio = anuncioService.findById(anuncioId);
        if (anuncio == null) {
            throw new IllegalArgumentException("Anuncio no encontrado");
        }
        boolean exito = usuarioService.agregarFavorito(usuario, anuncio);
        if (!exito) {
            throw new IllegalArgumentException("El anuncio ya está en favoritos");
        }
    }

    private void actualizarUsuarioEnSesion(HttpServletRequest request, Long userId) {
        Usuario usuarioActualizado = usuarioService.findById(userId);
        request.getSession().setAttribute("usuario", usuarioActualizado);
    }
}
