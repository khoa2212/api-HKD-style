FROM openjdk:17-alpine
WORKDIR /app
COPY build/libs/*.jar /app/user-service.jar
ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]