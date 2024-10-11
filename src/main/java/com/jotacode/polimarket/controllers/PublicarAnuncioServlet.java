package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.model.dao.AnuncioDAO;
import com.jotacode.polimarket.model.dao.UsuarioDAO;
import com.jotacode.polimarket.model.entity.Anuncio;
import com.jotacode.polimarket.model.entity.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/publicarAnuncio")
public class PublicarAnuncioServlet extends HttpServlet {

    private AnuncioDAO anuncioDAO;
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        anuncioDAO = new AnuncioDAO();
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Mostrar el formulario de publicación de anuncio
        request.getRequestDispatcher("/WEB-INF/views/publicarAnuncio.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener parámetros del formulario
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String precioStr = request.getParameter("precio");
        String imagen = request.getParameter("imagen");
        String emailUsuario = request.getParameter("emailUsuario");
        String username = request.getParameter("username");
        String telefono = request.getParameter("telefono");

        // Validar y convertir datos
        BigDecimal precio = null;
        try {
            precio = new BigDecimal(precioStr);
        } catch (NumberFormatException e) {
            // Manejar el error de formato de precio
            request.setAttribute("error", "Precio inválido.");
            request.getRequestDispatcher("/WEB-INF/views/publicarAnuncio.jsp").forward(request, response);
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

        // Crear un nuevo anuncio
        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo(titulo);
        anuncio.setDescripcion(descripcion);
        anuncio.setPrecio(precio);
        anuncio.setImagen(imagen);
        anuncio.setUsuario(usuario);

        // Guardar el anuncio
        anuncioDAO.saveAnuncio(anuncio);

        // Redirigir a la página de anuncios
        response.sendRedirect(request.getContextPath() + "/verAnuncios");
    }
}
