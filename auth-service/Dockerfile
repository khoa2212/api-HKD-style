FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar auth-service.jar
ENTRYPOINT ["java", "-jar", "/app/auth-service.jar"]