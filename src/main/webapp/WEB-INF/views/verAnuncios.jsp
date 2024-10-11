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
        <title>Anuncios Disponibles</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <header>
            <h1>Anuncios Disponibles</h1>
        </header>
        <div class="container">
            <a href="${pageContext.request.contextPath}/publicarAnuncio" class="btn-secondary">Publicar Nuevo Anuncio</a>
            <a href="${pageContext.request.contextPath}/filtrarAnuncios" class="btn-secondary">Filtrar Anuncios</a>
            <br><br>
            <c:if test="${empty anuncios}">
                <p>No hay anuncios disponibles.</p>
            </c:if>
            <c:if test="${not empty anuncios}">
                <c:forEach var="anuncio" items="${anuncios}">
                    <div class="anuncio">
                        <h2>${anuncio.titulo}</h2>
                        <p>${anuncio.descripcion}</p>
                        <p>Precio: $${anuncio.precio}</p>
                        <p>Publicado por: ${anuncio.usuario.username} (${anuncio.usuario.email})</p>
                        <a href="${pageContext.request.contextPath}/verAnuncio?id=${anuncio.idAnuncio}">Ver Detalles</a>
                    </div>
                </c:forEach>
            </c:if>
        </div>
    </body>
</html>

