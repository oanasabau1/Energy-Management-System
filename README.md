# Energy Management System  

This repository contains a progressively developed **Energy Management System** project for the **Distributed Systems** course, showcasing the integration of various distributed systems concepts and technologies. The system provides functionalities for energy data management, monitoring, real-time communication, and secure access control.  

## Technologies Used  
- **Backend:** Spring Boot  
- **Frontend:** ReactJS  
- **Database:** MySQL  
- **Messaging System:** RabbitMQ  
- **Authentication:** JWT Tokens for secure endpoint access  
- **Containerization:** Docker  
- **Load Balancing and Reverse Proxy:** Traefik  
- **Communication Protocols:** REST APIs and WebSockets  

## Branch Information  

### 1. `assignment_1`  
The initial branch lays the foundation of the Energy Management System with:  
- Implementation of **Chat and Device Management Microservices**.  
- Role-Based Access Control (RBAC) with **Admin** and **Client** roles.  
- Docker containerization for streamlined deployment.  

### 2. `assignment_2`  
The second branch introduces:  
- A **Monitoring Microservice** for real-time energy consumption tracking.  
- **Data Visualization** for device energy consumption through charts.  
- Push notifications when hourly energy consumption exceeds defined thresholds via WebSockets.  
- **Load Balancing and Reverse Proxy** using Traefik for efficient traffic routing and scalability.  

### 3. `assignment_3`  
The third branch further enhances the system with:  
- A **Real-Time Chat Microservice** enabling communication between users and admin via WebSockets.  
- **Authorization for All Endpoints** using JWT Tokens, ensuring secure access control across the application.  

## How to Run the Application  
1. Clone the repository.
3. Checkout the desired branch:
   ```bash
   git checkout main
4. Create the network:

   ```bash
   docker network create 172.30.0.0 demo-net
  6. Build the containers:
     
     ```bash
     docker-compose up --build


