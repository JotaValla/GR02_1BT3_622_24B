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

        // Validaciones
        if (!validarPassword(password)) {
            mostrarError(request, response,
                    "La contraseña debe tener entre 8 y 16 caracteres, al menos una mayúscula, una minúscula y un número");
            return;
        }

        if (!validarNombre(nombre)) {
            mostrarError(request, response,
                    "El nombre debe tener mínimo 3 caracteres y solo contener letras, tildes y espacios");
            return;
        }

        if (!validarFormatoUsername(usernameCuenta)) {
            mostrarError(request, response, "El nombre de usuario debe tener entre 3 y 15 caracteres. Solo puede contener letras y números");
            return;
        }

        // Si todas las validaciones pasan, crear cuenta y usuario
        crearCuentaYUsuario(usernameCuenta, password, nombre, telefono, email);
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private boolean validarPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{6,16}$";
        return password.matches(passwordRegex);
    }

    private boolean validarNombre(String nombre) {
        String nombreRegex = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]{3,}(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$";
        return nombre.matches(nombreRegex);
    }

    private boolean validarFormatoUsername(String username) {
        String usernameRegex = "^[a-zA-Z0-9]{3,15}$";
        return username.matches(usernameRegex);
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
    }

    private void crearCuentaYUsuario(String usernameCuenta, String password, String nombre, String telefono,
            String email) {
        Cuenta cuenta = cuentaService.crearCuenta(usernameCuenta, password);
        usuarioService.crearUsuario(nombre, telefono, email, cuenta);
    }

}
