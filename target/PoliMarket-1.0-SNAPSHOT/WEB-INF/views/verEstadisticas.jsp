<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.jotacode.polimarket.models.entity.Anuncio" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Estadísticas del Anuncio</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <div class="container">
            <h1>Estadísticas del Anuncio</h1>

            <%
                Anuncio anuncio = (Anuncio) request.getAttribute("anuncio");
                Double promedioValoraciones = (Double) request.getAttribute("promedioValoraciones");
                if (anuncio != null) {
            %>
            <h2><%= anuncio.getTitulo() %>
            </h2>
            <p>Vistas: <%= anuncio.getVistas() %>
            </p>
            <p>Promedio de Valoraciones: <%= String.format("%.2f", promedioValoraciones) %> estrellas</p>
            <%
            } else {
            %>
            <p>No se encontraron estadísticas para este anuncio.</p>
            <%
                }
            %>

            <button onclick="window.location.href='${pageContext.request.contextPath}/menu'">Volver al Menú</button>
        </div>
    </body>
</html>
