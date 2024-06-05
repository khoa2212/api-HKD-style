FROM openjdk:17-alpine
WORKDIR /app
COPY build/libs/*.jar /app/webapi.jar
ENTRYPOINT ["java", "-jar", "/app/webapi.jar"]