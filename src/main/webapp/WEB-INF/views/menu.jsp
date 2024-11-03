<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="com.jotacode.polimarket.models.entity.Usuario" %>
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
    <body>
        <header>
            <h1>Menú Principal</h1>
            <h2>Bienvenido, <%= usuario.getNombre() %></h2>
        </header>

        <!-- Contenedor principal con las opciones del menú -->
        <div class="container">
            <div style="text-align: center;">
                <a href="${pageContext.request.contextPath}/publicarAnuncio" class="btn-secondary"
                   style="margin: 10px;">Publicar Anuncio</a>
                <a href="${pageContext.request.contextPath}/verAnuncios" class="btn-secondary" style="margin: 10px;">Ver
                    Anuncios</a>
                <a href="${pageContext.request.contextPath}/filtrarAnuncios" class="btn-secondary"
                   style="margin: 10px;">Filtrar Anuncios</a>
                <a href="${pageContext.request.contextPath}/publicarValoracion" class="btn-secondary"
                   style="margin: 10px;">Publicar Valoración</a>
            </div>
        </div>

        <!-- Nuevo contenedor para gestionar cuenta -->
        <div class="container cuenta-container">
            <h3>Gestionar Cuenta</h3>
            <div class="account-actions">
                <button onclick="window.location.href='${pageContext.request.contextPath}/actualizarContrasena'" class="btn-secondary">
                    Actualizar Contraseña
                </button>
                <button onclick="window.location.href='${pageContext.request.contextPath}/login'" class="btn-secondary">
                    Cerrar Sesión
                </button>
            </div>
        </div>
    </body>
</html>
