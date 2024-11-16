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

@WebServlet("/eliminarFavorito")
public class EliminarFavoritoServlet extends HttpServlet {

    private static final String LOGIN_PAGE = "login.jsp";
    private static final String FAVORITOS_PAGE = "favoritos";

    private final UsuarioService usuarioService;
    private final AnuncioService anuncioService;

    public EliminarFavoritoServlet() {
        this.usuarioService = new UsuarioService();
        this.anuncioService = new AnuncioService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Validar usuario autenticado
            Usuario usuario = obtenerUsuarioDeSesion(request);
            // Validar y obtener el ID del anuncio
            Long anuncioId = obtenerAnuncioId(request);
            // Eliminar el anuncio de favoritos
            eliminarFavorito(usuario, anuncioId);
            // Actualizar usuario en sesión
            actualizarUsuarioEnSesion(request, usuario.getIdUsuario());
            // Redirigir con éxito
            response.sendRedirect(FAVORITOS_PAGE + "?deleteStatus=success");
        } catch (IllegalArgumentException e) {
            response.sendRedirect(FAVORITOS_PAGE + "?deleteStatus=failure");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(FAVORITOS_PAGE + "?deleteStatus=failure");
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

    private void eliminarFavorito(Usuario usuario, Long anuncioId) throws Exception {
        Anuncio anuncio = anuncioService.findById(anuncioId);
        if (anuncio == null) {
            throw new IllegalArgumentException("Anuncio no encontrado");
        }
        usuarioService.eliminarFavorito(usuario, anuncio);
    }

    private void actualizarUsuarioEnSesion(HttpServletRequest request, Long userId) {
        Usuario usuarioActualizado = usuarioService.findById(userId);
        request.getSession().setAttribute("usuario", usuarioActualizado);
    }
}
