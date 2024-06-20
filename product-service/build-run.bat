@echo off
.\gradlew bootJar && ^
cd .. && ^
docker compose up -d --build product-service
