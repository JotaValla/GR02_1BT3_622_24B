<%@page import="com.jotacode.polimarket.models.entity.Valoracion" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Editar Valoración</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <div class="container">
            <h1>Editar Valoración</h1>

            <%
                Valoracion valoracion = (Valoracion) request.getAttribute("valoracion");
            %>

            <form action="${pageContext.request.contextPath}/editarValoracion" method="post">
                <input type="hidden" name="id" value="<%= valoracion.getIdValoracion() %>">

                <label for="estrellas">Estrellas:</label>
                <input type="number" id="estrellas" name="estrellas" min="1" max="5" value="<%= valoracion.getEstrellas() %>" required><br>

                <label for="comentario">Comentario:</label>
                <textarea id="comentario" name="comentario" required><%= valoracion.getComentario() %></textarea><br>

                <input type="submit" value="Guardar Cambios">
            </form>
            <br>
            <button onclick="window.location.href='${pageContext.request.contextPath}/misValoraciones'">Cancelar</button>
        </div>
    </body>
</html>
