@echo off
.\gradlew bootJar && ^
docker compose up -d --build