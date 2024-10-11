package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.model.dao.AnuncioDAO;
import com.jotacode.polimarket.model.dao.UsuarioDAO;
import com.jotacode.polimarket.model.dao.ValoracionDAO;
import com.jotacode.polimarket.model.entity.Anuncio;
import com.jotacode.polimarket.model.entity.Usuario;
import com.jotacode.polimarket.model.entity.Valoracion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/valoraciones")
public class ValoracionServlet extends HttpServlet {

    private ValoracionDAO valoracionDAO;
    private AnuncioDAO anuncioDAO;
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        valoracionDAO = new ValoracionDAO();
        anuncioDAO = new AnuncioDAO();
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Mostrar el formulario de valoración
        String anuncioIdStr = request.getParameter("anuncioId");
        if (anuncioIdStr == null || anuncioIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/verAnuncios");
            return;
        }

        request.setAttribute("anuncioId", anuncioIdStr);
        request.getRequestDispatcher("/WEB-INF/views/publicarValoracion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener parámetros del formulario
        String estrellasStr = request.getParameter("estrellas");
        String comentario = request.getParameter("comentario");
        String anuncioIdStr = request.getParameter("anuncioId");
        String emailUsuario = request.getParameter("emailUsuario");
        String username = request.getParameter("username");
        String telefono = request.getParameter("telefono");

        // Validar y convertir datos
        Integer estrellas = null;
        Long anuncioId = null;
        try {
            estrellas = Integer.parseInt(estrellasStr);
            anuncioId = Long.parseLong(anuncioIdStr);
            if (estrellas < 1 || estrellas > 5) {
                throw new NumberFormatException("Estrellas fuera de rango.");
            }
        } catch (NumberFormatException e) {
            // Manejar errores de validación
            request.setAttribute("error", "Datos de valoración inválidos.");
            request.setAttribute("anuncioId", anuncioIdStr);
            request.getRequestDispatcher("/WEB-INF/views/publicarValoracion.jsp").forward(request, response);
            return;
        }

        // Buscar si el usuario ya existe por email
        Usuario usuario = usuarioDAO.getUsuarios().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(emailUsuario))
                .findFirst()
                .orElse(null);

        if (usuario == null) {
            // Crear un nuevo usuario si no existe
            usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setEmail(emailUsuario);
            usuario.setTelefono(telefono);
            usuarioDAO.saveUsuario(usuario);
        }

        // Buscar el anuncio correspondiente
        Anuncio anuncio = anuncioDAO.getAnuncioById(anuncioId);
        if (anuncio == null) {
            request.setAttribute("error", "Anuncio no encontrado.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }

        // Crear una nueva valoración
        Valoracion valoracion = new Valoracion();
        valoracion.setEstrellas(estrellas);
        valoracion.setComentario(comentario);
        valoracion.setAnuncio(anuncio);
        valoracion.setUsuario(usuario);

        // Guardar la valoración
        valoracionDAO.saveValoracion(valoracion);

        // Redirigir a la página de detalles del anuncio
        response.sendRedirect(request.getContextPath() + "/verAnuncio?id=" + anuncioId);
    }
}

