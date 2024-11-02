<%@page import="com.jotacode.polimarket.models.entity.Anuncio" %>
<%@page import="com.jotacode.polimarket.models.entity.Valoracion" %>
<%@page import="java.util.List" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Valoraciones del Anuncio</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <div class="container">
            <h1>Valoraciones del Anuncio</h1>

            <%
                Anuncio anuncio = (Anuncio) request.getAttribute("anuncio");
                if (anuncio != null) {
            %>
            <div class="anuncio-info">
                <h2><%=anuncio.getTitulo()%>
                </h2>
                <!-- Mostrar la imagen del anuncio -->
                <img src="${pageContext.request.contextPath}/uploads/<%= anuncio.getImagen().substring(anuncio.getImagen().lastIndexOf("/") + 1) %>"
                     alt="<%= anuncio.getTitulo() %>">
                <p><%=anuncio.getDescripcion()%>
                </p>
                <p>Precio: $<%=anuncio.getPrecio()%>
                </p>
                <p>Publicado por: <%=anuncio.getUsuAnuncio().getNombre()%>
                </p>
            </div>

            <h3>Valoraciones:</h3>
            <%
                List<Valoracion> valoraciones = (List<Valoracion>) request.getAttribute("valoraciones");
                if (valoraciones != null && !valoraciones.isEmpty()) {
                    for (Valoracion valoracion : valoraciones) {
            %>
            <div class="valoracion">
                <p>Usuario: <%=valoracion.getUsuValoracion().getNombre()%>
                </p>
                <p>Estrellas: <%=valoracion.getEstrellas()%> ⭐</p>
                <p>Comentario: <%=valoracion.getComentario()%>
                </p>
            </div>
            <%
                }
            } else {
            %>
            <p>No hay valoraciones para este anuncio.</p>
            <%
                }
            } else {
            %>
            <p>No se encontró información del anuncio.</p>
            <%
                }
            %>

            <br>
            <button onclick="window.location.href='${pageContext.request.contextPath}/verAnuncios'">Volver a Anuncios
            </button>
            <button onclick="window.location.href='${pageContext.request.contextPath}/menu'">Volver al Menú</button>

        </div>
    </body>
</html>