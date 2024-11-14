<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Publicar Anuncio</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body class = "login-page">
        <div class="container">
            <h1>Publicar Anuncio</h1>

            <!-- Mostrar mensaje de éxito o error -->
            <%
                String successMessage = (String) request.getAttribute("successMessage");
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (successMessage != null) {
            %>
            <div class="mensaje-exito"><%= successMessage %></div>
            <%
            } else if (errorMessage != null) {
            %>
            <div class="mensaje-error"><%= errorMessage %></div>
            <%
                }
            %>

            <form action="${pageContext.request.contextPath}/publicarAnuncio" method="post" enctype="multipart/form-data">
                <label for="titulo">Título:</label>
                <input type="text" id="titulo" name="titulo" value="${param.titulo}" required><br>

                <label for="descripcion">Descripción:</label>
                <textarea id="descripcion" name="descripcion" required>${param.descripcion}</textarea><br>

                <label for="imagen">Imagen:  (Recuerda que la imagen no es obligatoria)   </label>
                <input type="file" id="imagen" name="imagen" accept="image/png, image/jpeg"><br>

                <label for="precio">Precio:</label>
                <input type="number" id="precio" name="precio" step="0.01" value="${param.precio}" required><br>

                <label for="categoria">Categoría:</label>
                <select id="categoria" name="categoria" required>
                    <option value="electronica" ${param.categoria == 'electronica' ? 'selected' : ''}>Electronica</option>
                    <option value="libros" ${param.categoria == 'libros' ? 'selected' : ''}>Libros</option>
                    <option value="ropa" ${param.categoria == 'ropa' ? 'selected' : ''}>Ropa</option>
                    <option value="todos" ${param.categoria == 'todos' ? 'selected' : ''}>Todos</option>
                    <option value="comida" ${param.categoria == 'comida' ? 'selected' : ''}>Comida</option>
                </select><br>

                <input type="submit" value="Publicar Anuncio">
                <br>
                <button onclick="window.location.href='${pageContext.request.contextPath}/menu'">Volver al Menú</button>
            </form>
        </div>
    </body>
</html>
