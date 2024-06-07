## Development guidelines:
* Build the code into jar file  
    `.\gradlew bootJar`
* Build image and start container  
    `docker compose up -d --build`
* Push the ready-to-deploy image to Dockerhub  
    `docker push makiseemi/hkd-gateway`