<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="com.jotacode.polimarket.models.entity.Usuario" %>
<%@ page import="com.jotacode.polimarket.models.entity.Anuncio" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");

    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Polimarket - Menú Principal</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body  class = "login-page">
        <header>
            <h1>Menú Principal</h1>
            <h2>Bienvenido, <%= usuario.getNombre() %>
            </h2>
        </header>

        <!-- Opciones principales -->
        <div class="container">
            <div style="text-align: center;">
                <a href="${pageContext.request.contextPath}/publicarAnuncio" class="btn-secondary" style="margin: 10px;">Publicar Anuncio</a>
                <a href="${pageContext.request.contextPath}/verAnuncios" class="btn-secondary" style="margin: 10px;">Ver Anuncios</a>
                <a href="${pageContext.request.contextPath}/misValoraciones" class="btn-secondary" style="margin: 10px;">Mis Valoraciones</a>
                <a href="${pageContext.request.contextPath}/favoritos" class="btn-secondary" style="margin: 10px;">Mis Favoritos</a>
            </div>
        </div>

        <!-- Mostrar los anuncios del usuario con imagen y detalles -->
        <div class="container">
            <h3>Mis Anuncios</h3>
            <%
                if (usuario.getAnuncios() != null && !usuario.getAnuncios().isEmpty()) {
                    for (Anuncio anuncio : usuario.getAnuncios()) {
            %>
            <div class="anuncio">
                <%
                    String imagen = anuncio.getImagen();
                    String imagenSrc = imagen != null ? request.getContextPath() + "/uploads/" + imagen.substring(imagen.lastIndexOf("/") + 1) : request.getContextPath() + "/uploads/default.jpg";
                %>
                <img src="<%= imagenSrc %>" alt="<%= anuncio.getTitulo() %>" class="anuncio-img">
                <h4><%= anuncio.getTitulo() %></h4>
                <p>Precio: $<%= anuncio.getPrecio() %></p>
                <form action="${pageContext.request.contextPath}/verEstadisticas" method="get">
                    <input type="hidden" name="anuncioId" value="<%= anuncio.getIdAnuncio() %>">
                    <button type="submit">Ver Estadísticas</button>
                </form>
            </div>
            <%
                }
            } else {
            %>
            <p>No has publicado anuncios.</p>
            <%
                }
            %>
        </div>

        <!-- Gestión de cuenta -->
        <div class="container cuenta-container">
            <h3>Gestionar Cuenta</h3>
            <div class="account-actions">
                <button onclick="window.location.href='${pageContext.request.contextPath}/actualizarContrasena'"
                        class="btn-secondary">
                    Actualizar Contraseña
                </button>
                <button onclick="window.location.href='${pageContext.request.contextPath}/login'" class="btn-secondary">
                    Cerrar Sesión
                </button>
            </div>
        </div>
    </body>
</html>
