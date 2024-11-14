<%@page import="com.jotacode.polimarket.models.entity.Anuncio" %>
<%@page import="java.util.List" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Publicar Valoración</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <div class="container">
            <h1>Publicar Valoración</h1>

            <!-- Mostrar mensaje de éxito o error si existe -->
            <%
                String successMessage = (String) request.getAttribute("successMessage");
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (successMessage != null) {
            %>
            <div class="mensaje-exito"><%= successMessage %></div>
            <%
            } else if (errorMessage != null) {
            %>
            <div class="mensaje-error"><%= errorMessage %></div>
            <%
                }
            %>

            <form action="${pageContext.request.contextPath}/publicarValoracion" method="post">
                <%
                    String anuncioIdParam = request.getParameter("anuncioId");
                    if (anuncioIdParam != null) {
                        // Ocultamos el campo de selección de anuncio y usamos el anuncioId directamente
                %>
                <input type="hidden" name="anuncioId" value="<%= anuncioIdParam %>">

                <%
                } else {
                %>
                <!-- Este bloque de código solo se mostrará si no se recibe un anuncioId -->
                <label for="anuncioId">Seleccionar Anuncio:</label>
                <select id="anuncioId" name="anuncioId" required>
                    <%
                        List<Anuncio> anuncios = (List<Anuncio>) request.getAttribute("anuncios");
                        if (anuncios != null) {
                            for (Anuncio anuncio : anuncios) {
                    %>
                    <option value="<%=anuncio.getIdAnuncio()%>"><%=anuncio.getTitulo()%></option>
                    <%
                            }
                        }
                    %>
                </select><br>
                <%
                    }
                %>

                <label for="estrellas">Estrellas:</label>
                <input type="number" id="estrellas" name="estrellas" min="1" max="5" required><br>

                <label for="comentario">Comentario:</label>
                <textarea id="comentario" name="comentario" required></textarea><br>

                <input type="submit" value="Publicar">
            </form>
            <br>
            <button onclick="window.location.href='${pageContext.request.contextPath}/menu'">Volver al Menú</button>
        </div>
    </body>
</html>
