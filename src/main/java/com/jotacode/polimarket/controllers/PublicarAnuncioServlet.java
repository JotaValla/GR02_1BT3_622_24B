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

@WebServlet("/publicarAnuncio")
public class PublicarAnuncioServlet extends HttpServlet {
    private AnuncioDAO anuncioDAO;
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        anuncioDAO = new AnuncioDAO(null, Anuncio.class);
        usuarioDAO = new UsuarioDAO(null, Usuario.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/publicarAnuncio.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = createUserFromRequest(request);
        usuarioDAO.create(usuario);

        Anuncio anuncio = createAnuncioFromRequest(request, usuario);
        anuncioDAO.create(anuncio);

        response.sendRedirect(request.getContextPath() + "/verAnuncios");
    }

    private Usuario createUserFromRequest(HttpServletRequest request) {
        String username = request.getParameter("username");
        String foto = request.getParameter("foto");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setFoto(foto);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        return usuario;
    }

    private Anuncio createAnuncioFromRequest(HttpServletRequest request, Usuario usuario) {
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String imagen = request.getParameter("imagen");
        String categoria = request.getParameter("categoria");
        BigDecimal precio = new BigDecimal(request.getParameter("precio"));

        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo(titulo);
        anuncio.setDescripcion(descripcion);
        anuncio.setImagen(imagen);
        anuncio.setPrecio(precio);
        anuncio.setCategoria(categoria);
        anuncio.setUsuAnuncio(usuario);
        return anuncio;
    }

}