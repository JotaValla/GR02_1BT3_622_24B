<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>

<head>
    <title>Registro de Usuario</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>

<body>
    <div class="container">
        <h1>Registro de Usuario</h1>
        <form action="${pageContext.request.contextPath}/registro" method="post" onsubmit="return ValidarForm()">

            <h2>Información de la Cuenta</h2>
            <label for="usernameCuenta">Nombre de usuario (Cuenta):</label>
            <input type="text" id="usernameCuenta" name="usernameCuenta" required><br>

            <label for="password">Contraseña:</label>
            <input type="password" id="password" name="password" required><br>

            <h2>Información Personal del Usuario</h2>
            <label for="nombre">Nombre completo:</label>
            <input type="text" id="nombre" name="nombre" required><br>

            <label for="foto">URL de la foto de perfil:</label>
            <input type="text" id="foto" name="foto"><br>

            <label for="telefono">Teléfono:</label>
            <input type="tel" id="telefono" name="telefono" required><br>

            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required><br>

            <input type="submit" value="Registrarse">
            <br>
            <button onclick="window.location.href='${pageContext.request.contextPath}/'">Volver al Inicio</button>
        </form>
    </div>
</body>

</html>

<script>
    function validarForm() {
        var phone = document.getElementById("telefono").value;
        var profileUrl = document.getElementById("foto").value;
        var name = document.getElementById("nombre").value;

        // Verificación del teléfono
        var phonePattern = /^[0-9]{1,10}$/;
        if (!phonePattern.test(phone)) {
            alert("El teléfono debe contener solo números y hasta 10 dígitos.");
            return false;
        }

        // Verificación de la foto de perfil
        var urlPattern = /^(https?|ftp):\/\/[^\s/$.?#].[^\s]*$/i;
        if (!urlPattern.test(profileUrl)) {
            alert("La URL de la foto de perfil no es válida.");
            return false;
        }

        return true;
    }
</script>