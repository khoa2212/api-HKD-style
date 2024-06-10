# HKD Style API
## Prerequisites:
* Docker and Docker Compose are already installed in local machine

# How to use:
* **Create `hkd-network` network in docker**  
    You can ignore this step if the network is already existed  
    ```
    docker network create hkd-network
    ```
* **Initialize and start services**  
  ```
  docker compose up -d
  ```