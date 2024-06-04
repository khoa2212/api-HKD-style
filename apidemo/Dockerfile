FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar /app/api-demo.jar
ENTRYPOINT ["java", "-jar", "/app/api-demo.jar"]