<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<html>
    <head>
        <title>Registro de Usuario</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
        <script type="text/javascript">
            // Redireccionar automáticamente solo en caso de éxito
            window.onload = function () {
                const successMessage = document.querySelector(".mensaje-exito");

                if (successMessage) {
                    setTimeout(function () {
                        // Cambia la redirección para usar el servlet
                        window.location.href = "${pageContext.request.contextPath}/login";
                    }, 5000);
                }
            };
        </script>

    </head>

    <body class="login-page">
        <div class="container">
            <h1>Registro de Usuario</h1>

            <!-- Mostrar mensaje de éxito si existe -->
            <% if (request.getAttribute("success") != null) { %>
            <div class="mensaje-exito">
                <%= request.getAttribute("success") %>
            </div>
            <% } %>

            <!-- Mostrar mensaje de error si existe -->
            <% if (request.getAttribute("error") != null) { %>
            <div class="mensaje-error">
                <%= request.getAttribute("error") %>
            </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/registro" method="post">
                <h2>Información de la Cuenta</h2>
                <label for="usernameCuenta">Nombre de usuario (Cuenta):</label>
                <input type="text" id="usernameCuenta" name="usernameCuenta" value="${param.usernameCuenta}"
                       required><br>

                <label for="password">Contraseña:</label>
                <input type="password" id="password" name="password" required><br>

                <h2>Información Personal del Usuario</h2>
                <label for="nombre">Nombre completo:</label>
                <input type="text" id="nombre" name="nombre" value="${param.nombre}" required><br>

                <label for="telefono">Teléfono:</label>
                <input type="tel" id="telefono" name="telefono" value="${param.telefono}" maxlength="10"
                       pattern="[0-9]*" inputmode="numeric" required><br>

                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="${param.email}" required><br>

                <input type="submit" value="Confirmar">
                <br>
            </form>
            <br>
            <button onclick="window.location.href='${pageContext.request.contextPath}/index.jsp'">Regresar</button>
        </div>
    </body>
</html>
