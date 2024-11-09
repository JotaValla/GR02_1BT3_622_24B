<%@page import="com.jotacode.polimarket.models.entity.Anuncio" %>
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
                <!-- Mensaje de éxito al agregar a favoritos -->
                <% if (success) { %>
                    <div class="mensaje-exito">
                        ¡Anuncio agregado a favoritos con éxito!
                    </div>
                <% } else if (duplicate) { %>
                    <!-- Mensaje de error si el anuncio ya está en favoritos -->
                    <div class="mensaje-error">
                        No se puede añadir el mismo anuncio a tus favoritos dos veces.
                    </div>
                <% } %>

                <!-- Resto del contenido de verAnuncioCompleto.jsp -->
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
                <img src="<%= imagenSrc %>" alt="<%= anuncio.getTitulo() %>">
                <h2><%= anuncio.getTitulo() %>
                </h2>
                <p>Descripción: <%= anuncio.getDescripcion() %>
                </p>
                <p>Precio: $<%= anuncio.getPrecio() %>
                </p>
                <p>Publicado por: <%= anuncio.getUsuAnuncio().getNombre() %>
                </p>

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
            <%
            } else {
            %>
            <p>No se encontró el anuncio.</p>
            <%
                }
            %>
            <br>
            <button onclick="window.location.href='${pageContext.request.contextPath}/verAnuncios'">Volver a Anuncios
            </button>
        </div>

        <!-- Script para ocultar el mensaje después de unos segundos -->
        <script>
            window.onload = function() {
                const successAlert = document.querySelector('.alert-success');
                if (successAlert) {
                    setTimeout(() => {
                        successAlert.style.display = 'none';
                    }, 3000); // Oculta el mensaje después de 3 segundos
                }
            };
        </script>
    </body>
</html>
