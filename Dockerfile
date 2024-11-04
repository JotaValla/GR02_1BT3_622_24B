# Etapa 1: Construcción de la aplicación
FROM maven:3.8.5-openjdk-18-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve
COPY src /app/src
RUN mvn clean test package

# Etapa 2: Configuración de Tomcat para ejecutar la aplicación
FROM tomcat:10.1.8-jdk17

# Establecer el directorio de trabajo en Tomcat
WORKDIR /usr/local/tomcat

# Instalar wget y unzip para manejar la descarga y descompresión
RUN apt-get update && apt-get install -y wget unzip

# Descargar y extraer Derby en /opt/derby
RUN wget https://archive.apache.org/dist/db/derby/db-derby-10.16.1.1/db-derby-10.16.1.1-bin.zip && \
    unzip db-derby-10.16.1.1-bin.zip -d /opt/ && \
    mv /opt/db-derby-10.16.1.1-bin /opt/derby && \
    rm db-derby-10.16.1.1-bin.zip

# Crear un directorio para almacenar los datos de Derby
RUN mkdir -p /usr/local/tomcat/data

# Copiar el archivo WAR generado en la etapa de construcción
COPY --from=build /app/target/PoliMarket-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Copiar el archivo init.sql al contenedor
COPY init.sql /usr/local/tomcat/data/init.sql

# Configurar Derby como base de datos embebida y especificar el directorio de almacenamiento
ENV DERBY_HOME=/opt/derby
ENV DERBY_DATA_HOME=/usr/local/tomcat/data

# Exponer el puerto en el que correrá Tomcat
EXPOSE 8080

# Comando para inicializar la base de datos y luego arrancar Tomcat
CMD $DERBY_HOME/bin/ij /usr/local/tomcat/data/init.sql && catalina.sh run
