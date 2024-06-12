## Development guidelines:
* Run the `build-run.bat` file. This will build the source code into jar file which is then used to build a new image and start container
    ```
    .\build-run.bat
    ```
* Push the ready-to-deploy image to Dockerhub
    ```
    docker push makiseemi/hkd-webapi 
    ```