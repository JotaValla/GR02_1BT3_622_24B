package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.entity.Cuenta;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.services.CuentaService;
import com.jotacode.polimarket.services.UsuarioService;

import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private CuentaService cuentaService = new CuentaService();
    private UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Cuenta cuenta = cuentaService.findByUsernameAndPassword(username, password);

        if (cuenta != null) {
            Usuario usuario = usuarioService.findByCuenta(cuenta);

            if (usuario != null) {
                // Iniciar sesión si las credenciales son correctas
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);

                // Redirigir al menú principal usando RequestDispatcher
                request.getRequestDispatcher("/WEB-INF/views/menu.jsp").forward(request, response);
            } else {
                // Usuario no encontrado
                request.setAttribute("errorMessage", "No se encontró el usuario.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } else {
            // Si las credenciales son incorrectas
            request.setAttribute("errorMessage", "Nombre de usuario o contraseña incorrectos.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
