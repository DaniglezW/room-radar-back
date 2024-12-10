# Etapa de construcción
FROM amazoncorretto:22-alpine-jdk
# Copiar el archivo JAR generado desde la etapa de construcción
COPY ./target/practice-Repository-Server-0.0.1-SNAPSHOT.jar app.jar
# Definir el comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]