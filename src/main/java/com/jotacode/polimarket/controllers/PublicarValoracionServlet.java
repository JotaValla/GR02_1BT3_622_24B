package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.dao.AnuncioDAO;
import com.jotacode.polimarket.models.dao.UsuarioDAO;
import com.jotacode.polimarket.models.dao.ValoracionDAO;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/publicarValoracion")
public class PublicarValoracionServlet extends HttpServlet {

    private UsuarioService usuarioService;
    private AnuncioService anuncioService;
    private ValoracionService valoracionService;

    public PublicarValoracionServlet() {
        this.usuarioService = new UsuarioService();
        this.anuncioService = new AnuncioService();
        this.valoracionService = new ValoracionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Anuncio> anuncios = anuncioService.findAllAnuncios();
        request.setAttribute("anuncios", anuncios);
        request.getRequestDispatcher("/WEB-INF/views/publicarValoracion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Valoracion valoracion = createValoracionFromRequest(request);
        Usuario usuario = createUserFromRequest(request);

        Long anuncioId = Long.parseLong(request.getParameter("anuncioId"));

        Anuncio anuncio = anuncioService.findById(anuncioId);
        usuarioService.publicarValoracion(valoracion, anuncio, usuario);

        response.sendRedirect(request.getContextPath() + "/verAnuncios");
    }

    private Usuario createUserFromRequest(HttpServletRequest request) {
        String username = request.getParameter("username");
        String foto = request.getParameter("foto");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        return usuarioService.crearUsuario(username, foto, telefono, email);
    }

    private Valoracion createValoracionFromRequest(HttpServletRequest request) {
        Integer estrellas = Integer.parseInt(request.getParameter("estrellas"));
        String comentario = request.getParameter("comentario");
        Long anuncioId = Long.parseLong(request.getParameter("anuncioId"));
        return valoracionService.crearValoracion(estrellas, comentario);
    }

}