# Usa una imagen base de Tomcat
FROM tomcat:10.1.8-jdk17

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /usr/local/tomcat

# Copia el archivo WAR de la aplicación al directorio de aplicaciones web de Tomcat
COPY target/PoliMarket-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/

# Configura las variables de entorno para Apache Derby
ENV CLASSPATH /usr/local/tomcat/webapps/PoliMarket-1.0-SNAPSHOT.war:/usr/local/tomcat/lib/derbytools.jar

# Expone el puerto 8080 para el acceso a Tomcat desde fuera del contenedor
EXPOSE 8080

# Inicia Tomcat como proceso principal
CMD ["catalina.sh", "run"]
