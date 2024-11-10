package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.services.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {

    private static final String LOGIN_PAGE = "/login";
    private static final String MENU_PAGE = "/WEB-INF/views/menu.jsp";
    private static final String USUARIO_SESSION_ATTRIBUTE = "usuario";

    private UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Verificar si el usuario está autenticado
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_SESSION_ATTRIBUTE);

        if (usuario == null) {
            redirectToLogin(request, response);
            return;
        }

        // Recargar el usuario desde la base de datos para obtener los anuncios actualizados
        Usuario usuarioActualizado = usuarioService.findById(usuario.getIdUsuario());
        session.setAttribute(USUARIO_SESSION_ATTRIBUTE, usuarioActualizado);

        // Redirigir a menu.jsp
        forwardToMenu(request, response);
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + LOGIN_PAGE);
    }

    private void forwardToMenu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.getRequestDispatcher(MENU_PAGE).forward(request, response);
        } catch (ServletException | IOException e) {
            throw new ServletException("Error redirigiendo al menú.", e);
        }
    }
}
