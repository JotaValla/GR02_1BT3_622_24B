<%--
  Created by IntelliJ IDEA.
  User: djimm
  Date: 10/10/2024
  Time: 20:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Filtrar Anuncios</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <header>
            <h1>Filtrar Anuncios</h1>
        </header>
        <div class="container">
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <form action="${pageContext.request.contextPath}/filtrarAnuncios" method="post">
                <label for="titulo">Título contiene:</label>
                <input type="text" id="titulo" name="titulo">

                <label for="precioMin">Precio Mínimo:</label>
                <input type="number" id="precioMin" name="precioMin" step="0.01">

                <label for="precioMax">Precio Máximo:</label>
                <input type="number" id="precioMax" name="precioMax" step="0.01">

                <input type="submit" value="Filtrar">
            </form>
            <br>
            <a href="${pageContext.request.contextPath}/verAnuncios" class="btn-secondary">Volver a Todos los Anuncios</a>
        </div>
    </body>
</html>
