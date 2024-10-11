
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Publicar Valoración</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <header>
            <h1>Publicar Valoración</h1>
        </header>
        <div class="container">
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <form action="${pageContext.request.contextPath}/valoraciones" method="post">
                <input type="hidden" name="anuncioId" value="${anuncioId}">

                <label for="estrellas">Estrellas (1-5):</label>
                <input type="number" id="estrellas" name="estrellas" min="1" max="5" required>

                <label for="comentario">Comentario:</label>
                <textarea id="comentario" name="comentario" rows="4"></textarea>

                <h3>Información del Usuario</h3>

                <label for="username">Nombre de Usuario:</label>
                <input type="text" id="username" name="username" required>

                <label for="emailUsuario">Correo Electrónico:</label>
                <input type="email" id="emailUsuario" name="emailUsuario" required>

                <label for="telefono">Teléfono:</label>
                <input type="text" id="telefono" name="telefono">

                <input type="submit" value="Publicar Valoración">
            </form>
            <br>
            <a href="${pageContext.request.contextPath}/verAnuncios" class="btn-secondary">Volver a Anuncios</a>
        </div>
    </body>
</html>
