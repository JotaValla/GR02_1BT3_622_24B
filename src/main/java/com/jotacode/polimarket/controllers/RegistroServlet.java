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
        // Datos de la cuenta
        String usernameCuenta = request.getParameter("usernameCuenta");
        String password = request.getParameter("password");

        // Datos del usuario
        String nombre = request.getParameter("nombre");
        String foto = request.getParameter("foto");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        // Crear cuenta
        Cuenta cuenta = new Cuenta();
        cuenta.setUsername(usernameCuenta);
        cuenta.setPassword(password);

        cuenta = cuentaService.crearCuenta(usernameCuenta, password);
        usuarioService.crearUsuario(nombre, foto, telefono, email, cuenta);
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
