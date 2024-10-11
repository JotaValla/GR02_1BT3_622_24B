<%--
  Created by IntelliJ IDEA.
  User: djimm
  Date: 10/10/2024
  Time: 20:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Error</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <header>
            <h1>Error</h1>
        </header>
        <div class="container">
            <p>Lo sentimos, ha ocurrido un problema al procesar su solicitud. Por favor, inténtelo de nuevo más tarde.</p>
            <a href="${pageContext.request.contextPath}/verAnuncios" class="btn-secondary">Volver a la Página Principal</a>
        </div>
    </body>
</html>
