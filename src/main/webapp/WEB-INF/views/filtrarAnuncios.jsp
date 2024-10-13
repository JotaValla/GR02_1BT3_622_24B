<!-- /WEB-INF/views/filtrarAnuncios.jsp -->
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Filtrar Anuncios</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">

    </head>
    <body>
        <div class="container">
            <h1>Filtrar Anuncios</h1>
            <form action="${pageContext.request.contextPath}/filtrarAnuncios" method="post">
                <label for="categoria">Categoría:</label>
                <select id="categoria" name="categoria">
                    <option value="todos">Todos</option>
                    <option value="electronica">Electrónica</option>
                    <option value="libros">Libros</option>
                    <option value="ropa">Ropa</option>
                    <!-- Agrega más categorías según sea necesario -->
                </select>
                <input type="submit" value="Filtrar">
            </form>
            <br>
            <button onclick="window.location.href='${pageContext.request.contextPath}/'">Volver al Inicio</button>
        </div>
    </body>
</html>
