<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
    <head>
        <title>Registro de Usuario</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <div class="container">
            <h1>Registro de Usuario</h1>

            <!-- Mostrar mensajes de éxito o error -->
            <c:if test="${not empty mensajeExito}">
                <div class="mensaje-exito">
                        ${mensajeExito}
                    <c:if test="${not empty redirectToLogin}">
                        <!-- Formulario de redirección -->
                        <form action="${pageContext.request.contextPath}/login" method="get">
                            <input type="submit" value="Aceptar">
                        </form>
                    </c:if>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/registro" method="post">

                <h2>Información de la Cuenta</h2>
                <label for="usernameCuenta">Nombre de usuario (Cuenta):</label>
                <input type="text" id="usernameCuenta" name="usernameCuenta" required><br>

                <label for="password">Contraseña:</label>
                <input type="password" id="password" name="password" required><br>

                <h2>Información Personal del Usuario</h2>
                <label for="nombre">Nombre completo:</label>
                <input type="text" id="nombre" name="nombre" required><br>

                <label for="foto">URL de la foto de perfil:</label>
                <input type="text" id="foto" name="foto"><br>

                <label for="telefono">Teléfono:</label>
                <input type="tel" id="telefono" name="telefono" required><br>

                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required><br>

                <input type="submit" value="Registrarse">
                <br>
                <button onclick="window.location.href='${pageContext.request.contextPath}/'">Volver al Inicio</button>
            </form>
        </div>
    </body>
</html>
