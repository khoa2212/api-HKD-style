@echo off
.\gradlew bootJar && ^
cd .. && ^
docker compose up -d --build webapi