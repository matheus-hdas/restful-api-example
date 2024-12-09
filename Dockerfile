FROM eclipse-temurin:21
MAINTAINER github/matheus-hdas
COPY target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]