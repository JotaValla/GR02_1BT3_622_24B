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

    private UsuarioService usuarioService;
    private AnuncioService anuncioService;

    public EliminarFavoritoServlet() {
        this.usuarioService = new UsuarioService();
        this.anuncioService = new AnuncioService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String anuncioIdParam = request.getParameter("anuncioId");
        if (anuncioIdParam == null || anuncioIdParam.isEmpty()) {
            response.sendRedirect("favoritos");
            return;
        }

        try {
            Long anuncioId = Long.parseLong(anuncioIdParam);
            Anuncio anuncio = anuncioService.findById(anuncioId);

            if (anuncio != null) {
                usuarioService.eliminarFavorito(usuario, anuncio);

                // Recargar el usuario actualizado desde la base de datos para reflejar los cambios en favoritos
                Usuario usuarioActualizado = usuarioService.findById(usuario.getIdUsuario());
                request.getSession().setAttribute("usuario", usuarioActualizado);

                // Redirige a la página de favoritos con éxito en la eliminación
                response.sendRedirect("favoritos?deleteStatus=success");
            } else {
                // Si el anuncio no existe o no pudo ser encontrado
                response.sendRedirect("favoritos?deleteStatus=failure");
            }
        } catch (Exception e) {
            e.printStackTrace();

            response.sendRedirect("favoritos?deleteStatus=failure");
        }
    }
}
