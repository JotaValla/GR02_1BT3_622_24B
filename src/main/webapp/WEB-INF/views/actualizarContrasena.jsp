<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="com.jotacode.polimarket.models.entity.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");

    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Actualizar Contraseña</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <div class="container cuenta-container">
            <h3>Actualizar Contraseña</h3>

            <!-- Mostrar mensajes de éxito o error -->
            <%
                String successMessage = (String) request.getAttribute("successMessage");
                String errorMessage = (String) request.getAttribute("errorMessage");

                if (successMessage != null) {
            %>
            <div class="mensaje-exito"><%= successMessage %></div>
            <%
            } else if (errorMessage != null) {
            %>
            <div class="mensaje-error"><%= errorMessage %></div>
            <%
                }
            %>

            <form action="${pageContext.request.contextPath}/actualizarContrasena" method="post">
                <label for="currentPassword">Contraseña Actual:</label>
                <input type="password" id="currentPassword" name="currentPassword" required><br>

                <label for="newPassword">Nueva Contraseña:</label>
                <input type="password" id="newPassword" name="newPassword" required><br>

                <label for="confirmPassword">Confirmar Nueva Contraseña:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required><br>

                <input type="submit" value="Actualizar Contraseña" class="btn-secondary">
            </form>
            <br>
            <button onclick="window.location.href='${pageContext.request.contextPath}/menu'" class="btn-secondary">
                Volver al Menú
            </button>
        </div>
    </body>
</html>
