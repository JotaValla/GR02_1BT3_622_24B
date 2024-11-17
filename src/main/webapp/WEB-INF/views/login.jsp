<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Login</title>

        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body class = "login-page">
        <div class="container">
            <h1>Iniciar Sesión</h1>

            <!-- Para mostrar mensajes -->
            <c:if test="${not empty errorMessage}">
                <div class="error">${errorMessage}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <label for="username">Nombre de usuario:</label>
                <input type="text" id="username" name="username" required><br>

                <label for="password">Contraseña:</label>
                <input type="password" id="password" name="password" required><br>

                <input type="submit" value="Iniciar Sesión">
            </form><br>
            <button onclick="window.location.href='${pageContext.request.contextPath}/registro'">Registrarse</button>
        </div>
    </body>
</html>
