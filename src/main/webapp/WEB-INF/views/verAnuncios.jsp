<%@page import="com.jotacode.polimarket.model.entity.Anuncio" %>
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
            <%
                List<Anuncio> anuncios = (List<Anuncio>) request.getAttribute("anuncios");
                if (anuncios != null && !anuncios.isEmpty()) {
                    for (Anuncio anuncio : anuncios) {
            %>
            <div class="anuncio">
                <h2><%=anuncio.getTitulo()%>
                </h2>
                <img src="<%=anuncio.getImagen()%>" alt="<%=anuncio.getTitulo()%>">
                <p><%=anuncio.getDescripcion()%>
                </p>
                <p>Precio: $<%=anuncio.getPrecio()%>
                </p>
                <p>Publicado por: <%=anuncio.getUsuAnuncio().getUsername()%>
                </p>
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
            <button onclick="window.location.href='${pageContext.request.contextPath}/'">Volver al Inicio</button>
        </div>
    </body>
</html>