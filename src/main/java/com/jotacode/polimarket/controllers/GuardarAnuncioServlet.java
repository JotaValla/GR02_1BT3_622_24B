package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.dao.exceptions.NonexistentEntityException;
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
        Usuario usuario = obtenerUsuarioDeSesion(request, response);
        if (usuario == null) return;

        Long anuncioId = obtenerAnuncioId(request, response);
        if (anuncioId == null) return;

        try {
            if (!agregarAnuncioAFavoritos(usuario, anuncioId, response)) return;
        } catch (NonexistentEntityException e) {
            throw new RuntimeException(e);
        }

        actualizarUsuarioEnSesion(request, usuario.getIdUsuario());
        response.sendRedirect(String.format("%s?anuncioId=%d&success=true", VIEW_ANUNCIO_COMPLETO, anuncioId));
    }

    private Usuario obtenerUsuarioDeSesion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect(LOGIN_PAGE);
        }
        return usuario;
    }

    private Long obtenerAnuncioId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String anuncioIdParam = request.getParameter("anuncioId");
        if (anuncioIdParam == null || anuncioIdParam.isEmpty()) {
            response.sendRedirect(VIEW_ANUNCIOS);
            return null;
        }
        try {
            return Long.parseLong(anuncioIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(VIEW_ANUNCIOS);
            return null;
        }
    }

    private boolean agregarAnuncioAFavoritos(Usuario usuario, Long anuncioId, HttpServletResponse response) throws IOException, NonexistentEntityException {
        Anuncio anuncio = anuncioService.findById(anuncioId);
        if (anuncio == null) {
            response.sendRedirect(VIEW_ANUNCIOS);
            return false;
        }

        boolean exito = usuarioService.agregarFavorito(usuario, anuncio);
        if (!exito) {
            response.sendRedirect(String.format("%s?anuncioId=%d&success=false", VIEW_ANUNCIO_COMPLETO, anuncioId));
        }
        return exito;
    }

    private void actualizarUsuarioEnSesion(HttpServletRequest request, Long userId) {
        Usuario usuarioActualizado = usuarioService.findById(userId);
        request.getSession().setAttribute("usuario", usuarioActualizado);
    }
}
