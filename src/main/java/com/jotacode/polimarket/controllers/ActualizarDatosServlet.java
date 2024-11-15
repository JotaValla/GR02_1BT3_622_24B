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

        // Obtener parámetros del formulario
        String newPhone = request.getParameter("telefono");
        String newEmail = request.getParameter("email");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Inicializar servicios
        UsuarioService usuarioService = new UsuarioService();
        CuentaService cuentaService = new CuentaService();

        // Lógica de actualización y manejo de errores
        boolean infoUpdated = false;
        boolean passwordUpdated = false;

        // Actualizar información personal
        try {
            infoUpdated = usuarioService.updateUserInfo(usuario, newPhone, newEmail);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/actualizarDatos.jsp").forward(request, response);
            return;
        }

        // Actualizar contraseña
        if (currentPassword != null && !currentPassword.isEmpty()) {
            if (!cuentaService.validatePassword(usuario.getCuenta(), currentPassword)) {
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

            cuentaService.updatePassword(usuario.getCuenta(), newPassword);
            passwordUpdated = true;
        }

        // Mensajes de éxito
        if (infoUpdated && passwordUpdated) {
            request.setAttribute("successMessage", "La información de la cuenta y la contraseña se han actualizado exitosamente.");
        } else if (infoUpdated) {
            request.setAttribute("successMessage", "La información de la cuenta se ha actualizado exitosamente.");
        } else if (passwordUpdated) {
            request.setAttribute("successMessage", "La contraseña se ha actualizado exitosamente.");
        } else {
            request.setAttribute("successMessage", "No se realizaron cambios.");
        }

        request.getRequestDispatcher("/WEB-INF/views/actualizarDatos.jsp").forward(request, response);
    }

}
