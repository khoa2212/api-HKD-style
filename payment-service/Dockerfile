FROM openjdk:17-alpine
WORKDIR /app
COPY build/libs/*.jar /app/payment-service.jar
ENTRYPOINT ["java", "-jar", "/app/payment-service.jar"]