package com.jotacode.polimarket.controllers;

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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Verificar si el usuario está autenticado
        if (!isUserAuthenticated(request.getSession())) {
            redirectToLogin(request, response);
            return;
        }

        // Redirigir a menu.jsp
        forwardToMenu(request, response);
    }

    private boolean isUserAuthenticated(HttpSession session) {
        return session.getAttribute(USUARIO_SESSION_ATTRIBUTE) != null;
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
