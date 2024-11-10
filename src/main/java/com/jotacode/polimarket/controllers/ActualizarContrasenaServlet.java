package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.entity.Cuenta;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.services.CuentaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/actualizarContrasena")
public class ActualizarContrasenaServlet extends HttpServlet {

    private CuentaService cuentaService = new CuentaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/actualizarContrasena.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        Cuenta cuenta = usuario.getCuenta();

        // Validar contraseña actual
        if (!cuentaService.validatePassword(cuenta, currentPassword)) {
            request.setAttribute("errorMessage", "La contraseña actual es incorrecta.");
            request.getRequestDispatcher("/WEB-INF/views/actualizarContrasena.jsp").forward(request, response);
            return;
        }

        // Validar nueva contraseña y confirmación
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "La nueva contraseña y la confirmación no coinciden.");
            request.getRequestDispatcher("/WEB-INF/views/actualizarContrasena.jsp").forward(request, response);
            return;
        }

        // Validar requisitos de complejidad y coincidencia con la antigua contraseña
        if (!CuentaService.isValidPassword(newPassword)) {
            request.setAttribute("errorMessage", "La contraseña debe tener al menos 6 caracteres, una mayúscula y un número.");
            request.getRequestDispatcher("/WEB-INF/views/actualizarContrasena.jsp").forward(request, response);
            return;
        }

        if (newPassword.equals(currentPassword)) {
            request.setAttribute("errorMessage", "La nueva contraseña no puede ser igual a la anterior.");
            request.getRequestDispatcher("/WEB-INF/views/actualizarContrasena.jsp").forward(request, response);
            return;
        }

        // Actualizar contraseña y mostrar mensaje de éxito
        cuentaService.updatePassword(cuenta, newPassword);
        request.setAttribute("successMessage", "La contraseña se ha actualizado exitosamente.");
        request.getRequestDispatcher("/WEB-INF/views/actualizarContrasena.jsp").forward(request, response);
    }

}
