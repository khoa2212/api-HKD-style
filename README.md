# HKD Style API
## Prerequisites:
* Docker and Docker Compose are already installed in local machine

## How to use:
* **Create `hkd-network` network in docker**  
    You can ignore this step if the network is already existed  
    ```
    docker network create hkd-network
    ```
* **Remember to change username and password to connect to the database as well as the `SECRET_KEY` in the `.env` file** 
* **Initialize and start services**  
  ```
  docker compose up -d
  ```
## Developement guidelines
When developing a new service, just simply run the batch file `build-run.bat` in each service. Make sure that all the services have been started first by running `docker compose` in project root.
```
.\build-run.bat
```