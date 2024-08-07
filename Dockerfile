FROM openjdk:8

COPY target/*.jar /app.jar

EXPOSE 8080


ENTRYPOINT ["java", "-jar", "/app.jar", "--server.port=8080"]