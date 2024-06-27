FROM openjdk:17-alpine
WORKDIR /app
COPY build/libs/*.jar /app/email-service.jar
ENTRYPOINT ["java", "-jar", "/app/email-service.jar"]