# Usamos imagen oficial OpenJDK 17 con Alpine (ligera)
FROM eclipse-temurin:17-jdk-alpine

# Añadimos el jar compilado
COPY target/practice-Repository-Server-0.0.1-SNAPSHOT.jar app.jar

# Puerto que exponemos (cambia si tu app usa otro)
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java","-jar","/app.jar"]