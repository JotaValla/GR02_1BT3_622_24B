<%@page import="com.jotacode.polimarket.models.entity.Anuncio" %>
<%@page import="com.jotacode.polimarket.models.entity.Valoracion" %>
<%@page import="com.jotacode.polimarket.models.entity.Usuario" %>
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
                <h2><%=anuncio.getTitulo()%></h2>

                <!-- Controlar si la imagen es nula -->
                <%
                    String imagen = anuncio.getImagen();
                    String imagenSrc = imagen != null ? request.getContextPath() + "/uploads/" + imagen.substring(imagen.lastIndexOf("/") + 1) : request.getContextPath() + "/uploads/default.jpg";
                %>
                <img src="<%= imagenSrc %>" alt="<%= anuncio.getTitulo() %>">

                <p><%=anuncio.getDescripcion()%></p>
                <p>Precio: $<%=anuncio.getPrecio()%></p>
                <p>Publicado por: <%=anuncio.getUsuAnuncio().getNombre()%></p>
            </div>

            <h3>Valoraciones:</h3>
            <%
                List<Valoracion> valoraciones = (List<Valoracion>) request.getAttribute("valoraciones");
                if (valoraciones != null && !valoraciones.isEmpty()) {
                    for (Valoracion valoracion : valoraciones) {
            %>
            <div class="valoracion">
                <p>Usuario: <%=valoracion.getUsuValoracion().getNombre()%></p>
                <p>Estrellas: <%=valoracion.getEstrellas()%> ⭐</p>
                <p>Comentario: <%=valoracion.getComentario()%></p>
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
            <button onclick="window.location.href='${pageContext.request.contextPath}/verAnuncios'">Volver a Anuncios</button>
            <button onclick="window.location.href='${pageContext.request.contextPath}/menu'">Volver al Menú</button>

            <%
                Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
                boolean esPropietario = anuncio.getUsuAnuncio().getIdUsuario().equals(usuarioLogueado.getIdUsuario());
                Boolean haValorado = (Boolean) request.getAttribute("haValorado");
            %>

            <!-- Mostrar el botón solo si el usuario NO es el propietario y NO ha valorado -->
            <% if (!esPropietario && (haValorado == null || !haValorado)) { %>
                <button onclick="window.location.href='${pageContext.request.contextPath}/publicarValoracion?anuncioId=<%= anuncio.getIdAnuncio() %>'">
                    Publicar Nueva Valoración
                </button>
            <% } else if (haValorado != null && haValorado) { %>
                <p>Ya has valorado este anuncio.</p>
            <% } %>
        </div>
    </body>
</html>
