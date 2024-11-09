<%@page import="com.jotacode.polimarket.models.entity.Anuncio" %>
<%@page import="com.jotacode.polimarket.models.entity.Valoracion" %>
<%@page import="java.util.List" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Detalles del Anuncio</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <div class="container">
            <%
                String successParam = request.getParameter("success");
                boolean success = successParam != null && successParam.equals("true");
                boolean duplicate = successParam != null && successParam.equals("false");
            %>

            <div class="container">
                <% if (success) { %>
                <div class="mensaje-exito">
                    ¡Anuncio agregado a favoritos con éxito!
                </div>
                <% } else if (duplicate) { %>
                <div class="mensaje-error">
                    No se puede añadir el mismo anuncio a tus favoritos dos veces.
                </div>
                <% } %>
            </div>

            <%
                Anuncio anuncio = (Anuncio) request.getAttribute("anuncio");
                if (anuncio != null) {
            %>
            <div class="anuncio">
                <%
                    String imagen = anuncio.getImagen();
                    String imagenSrc = imagen != null ? request.getContextPath() + "/uploads/" + imagen.substring(imagen.lastIndexOf("/") + 1) : request.getContextPath() + "/uploads/default.jpg";
                %>
                <img src="<%= imagenSrc %>" alt="<%= anuncio.getTitulo() %>" class="imagen-original">
                <h2><%= anuncio.getTitulo() %></h2>
                <p>Descripción: <%= anuncio.getDescripcion() %></p>
                <p>Precio: $<%= anuncio.getPrecio() %></p>
                <p>Publicado por: <%= anuncio.getUsuAnuncio().getNombre() %></p>

                <!-- Botones adicionales -->
                <form action="${pageContext.request.contextPath}/verValoraciones" method="get">
                    <input type="hidden" name="anuncioId" value="<%= anuncio.getIdAnuncio() %>">
                    <button type="submit">Ver Valoraciones</button>
                </form>

                <form action="${pageContext.request.contextPath}/guardarAnuncio" method="post">
                    <input type="hidden" name="anuncioId" value="<%= anuncio.getIdAnuncio() %>">
                    <button type="submit">Guardar Anuncio</button>
                </form>
            </div>

            <h3>Valoraciones:</h3>
            <%
                List<Valoracion> valoraciones = (List<Valoracion>) request.getAttribute("valoraciones");
                if (valoraciones != null && !valoraciones.isEmpty()) {
                    for (Valoracion valoracion : valoraciones) {
            %>
            <div class="valoracion">
                <p>Usuario: <%= valoracion.getUsuValoracion().getNombre() %></p>
                <p>Estrellas: <%= valoracion.getEstrellas() %> ⭐</p>
                <p>Comentario: <%= valoracion.getComentario() %></p>
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
            <p>No se encontró el anuncio.</p>
            <%
                }
            %>
            <br>
            <button onclick="window.location.href='${pageContext.request.contextPath}/verAnuncios'">Volver a Anuncios</button>
        </div>

        <script>
            window.onload = function () {
                const successAlert = document.querySelector('.alert-success');
                if (successAlert) {
                    setTimeout(() => {
                        successAlert.style.display = 'none';
                    }, 3000);
                }
            };
        </script>
    </body>
</html>
