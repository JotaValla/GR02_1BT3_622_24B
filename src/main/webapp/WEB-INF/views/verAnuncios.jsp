<%@page import="com.jotacode.polimarket.models.entity.Anuncio" %>
<%@page import="com.jotacode.polimarket.models.entity.Usuario" %>
<%@page import="java.util.List" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ver Anuncios</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <h1>Anuncios</h1>

        <!-- Formulario para filtrar por usuario -->
        <form action="${pageContext.request.contextPath}/verAnuncios" method="get">
            <select name="usuarioId">
                <option value="">Todos los usuarios</option>
                <%
                    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
                    if (usuarios != null) {
                        for (Usuario usuario : usuarios) {
                %>
                <option value="<%= usuario.getIdUsuario() %>"><%= usuario.getNombre() %></option>
                <%
                        }
                    }
                %>
            </select>
            <button type="submit">Filtrar</button>
        </form>

        <%
            List<Anuncio> anuncios = (List<Anuncio>) request.getAttribute("anuncios");
            if (anuncios != null && !anuncios.isEmpty()) {
                for (Anuncio anuncio : anuncios) {
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
        <p>No hay anuncios disponibles.</p>
        <%
            }
        %>
        <br>
        <button onclick="window.location.href='${pageContext.request.contextPath}/menu'">Volver al Menú</button>
    </div>
</body>
</html>
