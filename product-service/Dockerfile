FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar product-service.jar
ENTRYPOINT ["java", "-jar", "/app/product-service.jar"]