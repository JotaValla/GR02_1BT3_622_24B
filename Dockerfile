# Etapa 1: Construcción de la aplicación
FROM maven:3.8.5-openjdk-18-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve
COPY src /app/src
RUN mvn clean package -DskipTests

# Etapa 2: Configuración de Tomcat para ejecutar la aplicación
FROM tomcat:10.1.8-jdk17
WORKDIR /usr/local/tomcat

# Copiar la aplicación WAR generada en la etapa de construcción
COPY --from=build /app/target/PoliMarket-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Copiar la imagen por defecto al directorio uploads dentro del contenedor
COPY resources/uploads/defaultAnuncio.jpg /usr/local/tomcat/uploads/defaultAnuncio.jpg

# Exponer el puerto en el que correrá Tomcat
EXPOSE 8080

# Iniciar Tomcat
CMD ["catalina.sh", "run"]
