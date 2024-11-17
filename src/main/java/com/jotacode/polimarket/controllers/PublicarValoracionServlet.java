package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.models.entity.Valoracion;
import com.jotacode.polimarket.services.AnuncioService;
import com.jotacode.polimarket.services.UsuarioService;
import com.jotacode.polimarket.services.ValoracionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/publicarValoracion")
public class PublicarValoracionServlet extends HttpServlet {

    private AnuncioService anuncioService = new AnuncioService();
    private ValoracionService valoracionService = new ValoracionService();
    private UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String anuncioIdParam = request.getParameter("anuncioId");
        if (anuncioIdParam != null) {
            // Si el anuncio ya está seleccionado, solo pasamos el anuncio a la vista
            Long anuncioId = Long.parseLong(anuncioIdParam);
            Anuncio anuncio = anuncioService.findById(anuncioId);
            request.setAttribute("anuncio", anuncio);
        } else {
            // Si no, mostramos todos los anuncios excluyendo los del usuario
            List<Anuncio> anuncios = anuncioService.findAllAnuncios();
            anuncios.removeIf(anuncio -> anuncio.getUsuAnuncio().getIdUsuario().equals(usuario.getIdUsuario()));
            request.setAttribute("anuncios", anuncios);
        }

        request.getRequestDispatcher("/WEB-INF/views/publicarValoracion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Integer estrellas = Integer.parseInt(request.getParameter("estrellas"));
        String comentario = request.getParameter("comentario");
        Long anuncioId = Long.parseLong(request.getParameter("anuncioId"));
        Anuncio anuncio = anuncioService.findById(anuncioId);

        // Verificar si el usuario ya ha valorado este anuncio
        if (valoracionService.existeValoracionDeUsuarioParaAnuncio(usuario.getIdUsuario(), anuncioId)) {
            request.setAttribute("errorMessage", "Ya has valorado este anuncio.");
        } else {
            try {
                // Crear la nueva valoración si no existe una anterior
                Valoracion valoracion = valoracionService.crearValoracion(estrellas, comentario);
                usuarioService.publicarValoracion(valoracion, anuncio, usuario);
                request.setAttribute("successMessage", "Valoración publicada correctamente.");
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Error al ingresar la valoración.");
            }
        }

        // Recargar los datos del anuncio para el formulario
        List<Anuncio> anuncios = anuncioService.findAllAnuncios();
        anuncios.removeIf(an -> an.getUsuAnuncio().getIdUsuario().equals(usuario.getIdUsuario())); // Excluir anuncios del usuario
        request.setAttribute("anuncios", anuncios);
        request.setAttribute("anuncio", anuncio);

        // Volver a la página de publicar valoración con el mensaje correspondiente
        request.getRequestDispatcher("/WEB-INF/views/publicarValoracion.jsp").forward(request, response);
    }


}
