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

            <!-- Mostrar mensaje de éxito o error al eliminar -->
            <%
                String deleteStatus = request.getParameter("deleteStatus");
                if ("success".equals(deleteStatus)) {
            %>
            <div class="mensaje-exito">Anuncio eliminado exitosamente de tus favoritos.</div>
            <%
            } else if ("failure".equals(deleteStatus)) {
            %>
            <div class="mensaje-error">No se pudo eliminar el anuncio de tus favoritos.</div>
            <%
                }
            %>

            <!-- Mostrar lista de anuncios guardados -->
            <%
                List<Anuncio> favoritos = (List<Anuncio>) request.getAttribute("favoritos");
                if (favoritos != null && !favoritos.isEmpty()) {
                    for (Anuncio anuncio : favoritos) {
            %>
            <div class="anuncio">
                <%
                    String imagen = anuncio.getImagen(); // Obtener la imagen
                    String imagenSrc = imagen != null ? request.getContextPath() + "/uploads/" + imagen.substring(imagen.lastIndexOf("/") + 1) : request.getContextPath() + "/uploads/default.jpg";
                %>
                <img src="<%= imagenSrc %>" alt="<%= anuncio.getTitulo() %>">
                <h2><%= anuncio.getTitulo() %>
                </h2>
                <p>Precio: $<%= anuncio.getPrecio() %>
                </p>
                <form action="${pageContext.request.contextPath}/verValoraciones" method="get" style="display: inline;">
                    <input type="hidden" name="anuncioId" value="<%= anuncio.getIdAnuncio() %>">
                    <button type="submit">Ver valoraciones</button>
                </form>
                <form action="${pageContext.request.contextPath}/eliminarFavorito" method="post"
                      style="display: inline;">
                    <input type="hidden" name="anuncioId" value="<%= anuncio.getIdAnuncio() %>">
                    <button type="submit">Eliminar de Favoritos</button>
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
