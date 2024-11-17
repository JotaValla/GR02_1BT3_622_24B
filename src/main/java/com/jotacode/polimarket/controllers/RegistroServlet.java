package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.services.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usernameCuenta = request.getParameter("usernameCuenta");
        String password = request.getParameter("password");
        String nombre = request.getParameter("nombre");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        try {
            // Validar datos de registro y crear cuenta/usuario
            usuarioService.validarDatosRegistro(usernameCuenta, password, nombre, email);
            usuarioService.crearUsuarioConCuenta(usernameCuenta, password, nombre, telefono, email);

            request.setAttribute("success", "Registro exitoso. Serás redirigido al inicio de sesión.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            mostrarError(request, response, e.getMessage());
        }
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
    }
}
