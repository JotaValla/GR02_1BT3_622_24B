package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.entity.Cuenta;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.services.CuentaService;
import com.jotacode.polimarket.services.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private CuentaService cuentaService = new CuentaService();
    private UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Extraemos los parámetros de la solicitud
        String usernameCuenta = request.getParameter("usernameCuenta");
        String password = request.getParameter("password");
        String nombre = request.getParameter("nombre");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        // Llamamos al nuevo método para crear cuenta y usuario
        crearCuentaYUsuario(usernameCuenta, password, nombre, telefono, email);

        // Redirigimos al login después de la creación
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void crearCuentaYUsuario(String usernameCuenta, String password, String nombre, String telefono, String email) {
        Cuenta cuenta = cuentaService.crearCuenta(usernameCuenta, password);
        usuarioService.crearUsuario(nombre, telefono, email, cuenta);
    }

}
