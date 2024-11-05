# Energy Management System 

## About

This project is the first part of the final project for the Distributed Systems course. The Energy Management System (EMS) is a web application that manages users and their associated smart energy metering devices, with a frontend and two backend microservices for efficient, modular operations. The system enables CRUD operations for users and devices, supporting both administrators and clients with distinct roles and permissions through Role-Based Access Control (RBAC).

### User Roles and Features

- **Administrator Role**:
  - **Manage Users**: Administrators can create, view, update, and delete user accounts.
  - **Manage Devices**: Administrators can create, view, update, and delete smart energy devices.
  - **Map Users to Devices**: Administrators can assign devices to users across different locations.
  
- **Client Role**:
  - **View Devices**: Clients have read-only access to view all devices assigned to them.

### Security

- **Role-Based Access Control (RBAC)**: Ensures that only authorized users can access specific parts of the system according to their role. Currently, basic access control is managed with `localStorage` on the frontend, and advanced security with OAuth and JWT will be added in future iterations.

## Technologies Used

- **Frontend**: ReactJS
- **Backend**: Java Spring Boot
- **Databases**: two MySQL servers for both user and device microservices
- **API**: RESTful APIs facilitate communication between the frontend and microservices. The User and Device microservices communicate using `WebClient`.
- **Container Orchestration**: Docker

## Docker Containerization

Docker is used to containerize the different components of EMS, making it easier to deploy, manage, and scale the application. By isolating each component in its own container, we ensure consistency and streamline deployment.

### 1. Containerizing the Components

Each component of project is defined in a **Dockerfile**, specifying its environment, dependencies, and commands to run within an isolated container.

#### Frontend (ReactJS)
- The container serves the React app and makes it accessible on a specified port (`3000`).

#### Backend Microservices (Java Spring Boot)
- Each microservice (User Management and Device Management) has a Dockerfile.
- Dockerfiles use a base image (e.g., `openjdk`), copy the compiled Spring Boot application (`.jar` file), and define commands to run it.
- Each microservice operates on its own port (`8080` and `8081`).

#### Databases (MySQL)
- Two MySQL containers are set up using an official MySQL Docker image.
- Environment variables (e.g., `MYSQL_ROOT_PASSWORD`, `MYSQL_DATABASE`) are set to configure the MySQL instances.
- Data persistence is managed by mapping volumes for storage, ensuring data is retained when the container restarts.

### 2. Docker Compose for Orchestration

Docker Compose manages the multi-container application. The `docker-compose.yml` file is used to orchestrate the frontend, backend microservices, and database containers so they can work together.

#### Docker Compose Configuration

- **Service Definitions**: Each component is defined as a service in `docker-compose.yml`.
- **Networking**: Docker Compose creates a network for the containers, allowing services to refer to each other by their service name as a hostname.
- **Environment Variables**: The configuration includes environment variables for database connections and service configurations. 
- **Port Mapping**: Each container's port is mapped to the host machine for access:
  - Frontend (`3000:3000`)
  - User Microservice (`8080:8080`)
  - Device Microservice (`8081:8081`)
  - MySQL User Database (`3307:3306`)
  - MySQL Device Database (`3308:3306`)

 
    ## Getting Started

Follow the steps below to set up and run my project on your local machine using Docker.

### Prerequisites

- Ensure you have Docker Desktop installed on your machine. Plase make sure that the app is already opened.

### Running the Application

First, make sure you have installed MySQL Workbench and create 2 new databases: `users` and `devices`. 
Then, clone the repository to your local machine in a desired project. 

   ```bash
git clone -b assignment_1 https://github.com/oanasabau1/Energy-Management-System.git
```

Now run the following commands to create a new network, to build the images from specific Dockerfiles and to create the containers:

   ```bash
docker network create --subnet=172.30.0.0/16 demo_net
docker-compose build
docker-compose up
```

If you want to remove the containers and stop the application, please run this command:
```bash
docker-compose down
``` 

### Overview

Here is the final result of the application from the GUI, where I have included all the pages from the frontend. If the user is not an admin and wants to access admin's page, the access will be denied.

![Screenshot 2024-11-05 125714](https://github.com/user-attachments/assets/2789c6c3-5e15-4ba3-a6a6-8f5307fce42c)
![Screenshot 2024-11-05 125658](https://github.com/user-attachments/assets/0a385b0c-f691-4672-ad99-e7457ed9dbfe)
![Screenshot 2024-11-05 130049](https://github.com/user-attachments/assets/23b75d3c-23c1-46d4-903a-b01fb224b208)
![Screenshot 2024-11-05 130032](https://github.com/user-attachments/assets/6028161c-50c2-481f-abff-33216637a777)
![Screenshot 2024-11-05 125917](https://github.com/user-attachments/assets/649d0981-e6bc-49a5-8612-ff893ff6690a)
![Screenshot 2024-11-05 125851](https://github.com/user-attachments/assets/e18c4245-d164-4828-a1dd-169d92143935)
![Screenshot 2024-11-05 125832](https://github.com/user-attachments/assets/d3396364-a1ba-4d81-a742-94ef4711818f)
![Screenshot 2024-11-05 125800](https://github.com/user-attachments/assets/03cf3275-7a4c-48da-9a87-253fafdbf6eb)
![Screenshot 2024-11-05 125739](https://github.com/user-attachments/assets/e97a296b-454f-41d4-8f0a-d79db8b1b4ad)


