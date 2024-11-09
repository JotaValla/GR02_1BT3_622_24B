<%@ page import="java.util.List" %>
<%@ page import="com.jotacode.polimarket.models.entity.Valoracion" %>
<%@ page import="com.jotacode.polimarket.models.entity.Anuncio" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Mis Valoraciones</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <div class="container">
            <h1>Mis Valoraciones</h1>

            <%
                List<Valoracion> valoraciones = (List<Valoracion>) request.getAttribute("valoraciones");
                if (valoraciones != null && !valoraciones.isEmpty()) {
                    for (Valoracion valoracion : valoraciones) {
            %>
            <div class="valoracion">
                <h3>Anuncio: <%= valoracion.getAnun().getTitulo() %></h3>
                <p>Estrellas: <%= valoracion.getEstrellas() %> ⭐</p>
                <p>Comentario: <%= valoracion.getComentario() %></p>

                <!-- Botón para editar la valoración -->
                <button onclick="window.location.href='${pageContext.request.contextPath}/editarValoracion?id=<%= valoracion.getIdValoracion() %>'">
                    Editar Valoración
                </button>
            </div>
            <hr>
            <%
                    }
                } else {
            %>
            <p>No has publicado ninguna valoración.</p>
            <%
                }
            %>

            <br>
            <button onclick="window.location.href='${pageContext.request.contextPath}/menu'">Volver al Menú</button>
        </div>
    </body>
</html>
