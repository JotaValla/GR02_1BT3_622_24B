# Fase 1: Construcción del proyecto
FROM maven:3.8.5-openjdk-18-slim AS build
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn clean install package -DskipTests

# Fase 2: Configuración del servidor Tomcat
FROM tomcat:10.1.23-jdk17
COPY --from=build /usr/src/app/target/PoliMarket-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/PoliMarket-1.0-SNAPSHOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
