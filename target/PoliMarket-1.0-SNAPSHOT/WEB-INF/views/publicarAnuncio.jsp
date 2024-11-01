<!-- publicarAnuncio.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Publicar Anuncio</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">

    </head>
    <body>
        <div class="container">
            <h1>Publicar Anuncio</h1>
            <form action="${pageContext.request.contextPath}/publicarAnuncio" method="post" enctype="multipart/form-data">
                <label for="titulo">Título:</label>
                <input type="text" id="titulo" name="titulo" required><br>

                <label for="descripcion">Descripción:</label>
                <textarea id="descripcion" name="descripcion" required></textarea><br>

                <label for="imagen">Imagen:</label>
                <input type="file" id="imagen" name="imagen" accept="image/*" required><br>

                <label for="precio">Precio:</label>
                <input type="number" id="precio" name="precio" step="0.01" required><br>

                <label for="categoria">Categoría:</label>
                <select id="categoria" name="categoria" required>
                    <option value="electronica">Electrónica</option>
                    <option value="libros">Libros</option>
                    <option value="ropa">Ropa</option>
                    <option value="todos">Todos</option>
                </select><br>

                <h2>Información del Usuario</h2>
                <label for="username">Nombre de usuario:</label>
                <input type="text" id="username" name="username" required><br>

                <label for="foto">URL de la foto de perfil:</label>
                <input type="text" id="foto" name="foto" required><br>

                <label for="telefono">Teléfono:</label>
                <input type="tel" id="telefono" name="telefono" required><br>

                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required><br>

                <input type="submit" value="Publicar Anuncio">
                <br>
                <button onclick="window.location.href='${pageContext.request.contextPath}/'">Volver al Inicio</button>
            </form>
        </div>
    </body>
</html>