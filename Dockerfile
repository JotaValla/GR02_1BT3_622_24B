# Etapa de construcción
FROM maven:3.8.5-openjdk-17-slim as build

# Copia del código fuente al contenedor
COPY . /usr/src/app
WORKDIR /usr/src/app

# Construcción de la aplicación
RUN mvn clean install
RUN mvn package

# Etapa de servidor
FROM bitnami/tomcat:10.1.23 as server

# Copia del archivo WAR generado en la etapa de construcción al contenedor Tomcat
COPY --from=build /usr/src/app/target/tuarchivo.war /opt/bitnami/tomcat/webapps/

# Exponer el puerto 8080 para la aplicación
EXPOSE 8080

# Comando para iniciar Tomcat
CMD ["catalina.sh", "run"]
