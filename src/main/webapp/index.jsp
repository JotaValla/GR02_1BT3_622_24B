<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link rel="icon" href="images/Logo_polimarket.png" type="image/png">
        <title>Polimarket - Inicio</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>


    <div class="contenedor-imagen">
        <img src="images/Logo_polimarket.png" alt=" carro de compras politécnicas">
    </div>

    <div class="container" style="text-align: center; margin-top: 50px;">
            <h1>Bienvenido a Polimarket</h1>
            <p>Por favor, inicia sesión para acceder al sistema.</p>
            <button onclick="window.location.href='${pageContext.request.contextPath}/login'">Iniciar Sesión</button>
            <br><br>
            <button onclick="window.location.href='${pageContext.request.contextPath}/registro'">Registrarse</button>
        </div>
    </body>
</html>
