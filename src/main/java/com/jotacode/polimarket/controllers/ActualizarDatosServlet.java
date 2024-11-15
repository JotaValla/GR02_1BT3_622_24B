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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/actualizarDatos")
public class ActualizarDatosServlet extends HttpServlet {

    private CuentaService cuentaService = new CuentaService();
    private UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/actualizarDatos.jsp").forward(request, response);
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
        String newPhone = request.getParameter("telefono");
        String newEmail = request.getParameter("email");

        Cuenta cuenta = usuario.getCuenta();

        // Password validation and update
        if (currentPassword != null && !currentPassword.isEmpty()) {
            if (!cuentaService.validatePassword(cuenta, currentPassword)) {
                request.setAttribute("errorMessage", "La contraseña actual es incorrecta.");
                request.getRequestDispatcher("/WEB-INF/views/actualizarDatos.jsp").forward(request, response);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("errorMessage", "La nueva contraseña y la confirmación no coinciden.");
                request.getRequestDispatcher("/WEB-INF/views/actualizarDatos.jsp").forward(request, response);
                return;
            }

            if (!CuentaService.isValidPassword(newPassword)) {
                request.setAttribute("errorMessage", "La contraseña debe tener al menos 6 caracteres, una mayúscula y un número.");
                request.getRequestDispatcher("/WEB-INF/views/actualizarDatos.jsp").forward(request, response);
                return;
            }

            if (newPassword.equals(currentPassword)) {
                request.setAttribute("errorMessage", "La nueva contraseña no puede ser igual a la anterior.");
                request.getRequestDispatcher("/WEB-INF/views/actualizarDatos.jsp").forward(request, response);
                return;
            }

            // Update password
            cuentaService.updatePassword(cuenta, newPassword);
        }


        // Update phone and email
        boolean updatedInfo = false;
        try {
            usuarioService.updateUserInfo(usuario, newPhone, newEmail);
            updatedInfo = true;
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/actualizarDatos.jsp").forward(request, response);
            return;
        }

        // Set success message based on changes
        if (updatedInfo) {
            request.setAttribute("successMessage", "La información de la cuenta se ha actualizado exitosamente.");
        } else if (newPassword != null && !newPassword.isEmpty()) {
            request.setAttribute("successMessage", "La contraseña se ha actualizado exitosamente.");
        } else {
            request.setAttribute("successMessage", "No hubo cambios en la información.");
        }

        request.getRequestDispatcher("/WEB-INF/views/actualizarDatos.jsp").forward(request, response);
    }
}
