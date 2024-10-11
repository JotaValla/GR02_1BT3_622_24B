<!-- /WEB-INF/views/publicarAnuncio.jsp -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Publicar Anuncio</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <header>
            <h1>Publicar Nuevo Anuncio</h1>
        </header>
        <div class="container">
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <form action="${pageContext.request.contextPath}/publicarAnuncio" method="post">
                <label for="titulo">Título:</label>
                <input type="text" id="titulo" name="titulo" required>

                <label for="descripcion">Descripción:</label>
                <textarea id="descripcion" name="descripcion" rows="4" required></textarea>

                <label for="precio">Precio:</label>
                <input type="number" id="precio" name="precio" step="0.01" required>

                <label for="imagen">URL de la Imagen:</label>
                <input type="text" id="imagen" name="imagen">

                <h3>Información del Usuario</h3>

                <label for="username">Nombre de Usuario:</label>
                <input type="text" id="username" name="username" required>

                <label for="emailUsuario">Correo Electrónico:</label>
                <input type="email" id="emailUsuario" name="emailUsuario" required>

                <label for="telefono">Teléfono:</label>
                <input type="text" id="telefono" name="telefono">

                <input type="submit" value="Publicar Anuncio">
            </form>
            <br>
            <a href="${pageContext.request.contextPath}/verAnuncios" class="btn-secondary">Volver a Anuncios</a>
        </div>
    </body>
</html>
