<%@page import="com.jotacode.polimarket.models.entity.Anuncio" %>
<%@page import="java.util.List" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Mis Anuncios Guardados</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <div class="container">
            <h1>Mis Anuncios Guardados</h1>

            <%
                List<Anuncio> favoritos = (List<Anuncio>) request.getAttribute("favoritos");
                if (favoritos != null && !favoritos.isEmpty()) {
                    for (Anuncio anuncio : favoritos) {
            %>
            <div class="anuncio">
                <h2><%= anuncio.getTitulo() %></h2>
                <%
                    String imagen = anuncio.getImagen(); // Obtener la imagen
                    String imagenSrc = imagen != null ? request.getContextPath() + "/uploads/" + imagen.substring(imagen.lastIndexOf("/") + 1) : request.getContextPath() + "/uploads/default.jpg";
                %>
                <img src="<%= imagenSrc %>" alt="<%= anuncio.getTitulo() %>">
                <p><%= anuncio.getDescripcion() %></p>
                <p>Precio: $<%= anuncio.getPrecio() %></p>
                <p>Publicado por: <%= anuncio.getUsuAnuncio().getNombre() %></p>
                <form action="${pageContext.request.contextPath}/verValoraciones" method="get">
                    <input type="hidden" name="anuncioId" value="<%= anuncio.getIdAnuncio() %>">
                    <button type="submit">Ver valoraciones</button>
                </form>
            </div>
            <%
                }
            } else {
            %>
            <p>No tienes anuncios guardados.</p>
            <%
                }
            %>
            <br>
            <button onclick="window.location.href='${pageContext.request.contextPath}/menu'">Volver al Menú</button>
        </div>
    </body>
</html>
