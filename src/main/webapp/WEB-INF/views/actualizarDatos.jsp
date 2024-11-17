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
        <title>Actualizar Datos</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <div class="container cuenta-container">
            <h3>Actualizar Datos de la Cuenta</h3>

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

            <!-- Formulario para Actualizar Información Personal -->
            <div class="info-section">
                <h2>Actualizar Información Personal</h2>
                <form action="${pageContext.request.contextPath}/actualizarDatos" method="post">
                    <label for="telefono">Teléfono:</label>
                    <input type="tel" id="telefono" name="telefono" value="<%= usuario.getTelefono() %>" maxlength="10" pattern="[0-9]*" inputmode="numeric" required><br>

                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" value="<%= usuario.getEmail() %>" required><br>

                    <input type="submit" value="Actualizar" class="btn-secondary">
                </form>
            </div>

            <!-- Formulario para Cambiar Contraseña -->
            <div class="password-section">
                <h2>Cambiar Contraseña</h2>
                <form action="${pageContext.request.contextPath}/actualizarDatos" method="post">
                    <label for="currentPassword">Contraseña Actual:</label>
                    <input type="password" id="currentPassword" name="currentPassword"><br>

                    <label for="newPassword">Nueva Contraseña:</label>
                    <input type="password" id="newPassword" name="newPassword"><br>

                    <label for="confirmPassword">Confirmar Nueva Contraseña:</label>
                    <input type="password" id="confirmPassword" name="confirmPassword"><br>

                    <input type="submit" value="Actualizar" class="btn-secondary">
                </form>
            </div>

            <br>
            <button onclick="window.location.href='${pageContext.request.contextPath}/menu'" class="btn-secondary">
                Volver al Menú
            </button>
        </div>
    </body>
</html>
