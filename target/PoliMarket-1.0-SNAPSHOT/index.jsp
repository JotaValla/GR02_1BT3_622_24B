<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Polimarket - Inicio</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
    <!-- Contenedor principal para dividir en dos columnas -->
    <div class="main-container">
        <!-- Contenedor para la parte izquierda (logo) -->
        <div class="left-side">
            <img src="images/Logo_fondo_polimarket.png" alt="Logo Polimarket">
        </div>

        <!-- Contenedor para la parte derecha (formulario) -->
        <div class="right-side">
            <div class="container">
                <h1>BIENVENID@</h1>
                <p>Por favor, inicia sesión para acceder al sistema.</p>
                <button onclick="window.location.href='${pageContext.request.contextPath}/login'">Iniciar Sesión</button>
                <br><br>
                <button onclick="window.location.href='${pageContext.request.contextPath}/registro'">Registrarse</button>
            </div>
        </div>
    </div>
    </body>
</html>
