<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Polimarket - Inicio</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <header>
            <h1>Bienvenido a Polimarket</h1>
        </header>
        <div class="container">
            <div style="text-align: center;">
                <a href="${pageContext.request.contextPath}/publicarAnuncio" class="btn-secondary" style="margin: 10px;">Publicar Anuncio</a>
                <a href="${pageContext.request.contextPath}/verAnuncios" class="btn-secondary" style="margin: 10px;">Ver Anuncios</a>
                <a href="${pageContext.request.contextPath}/filtrarAnuncios" class="btn-secondary" style="margin: 10px;">Filtrar Anuncios</a>
                <a href="${pageContext.request.contextPath}/publicarValoracion" class="btn-secondary" style="margin: 10px;">Publicar Valoración</a>
            </div>
        </div>
    </body>
</html>
