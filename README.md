# Energy-Management-System

This repository contains a gradually developed project for the **Distributed Systems** course: a web-based **Energy Management System. 

## Branch Information

For specific features and development stages, please explore the branches, as the project has been built incrementally.

### `assignment_1` Branch

The `assignment_1` branch contains the initial setup of the Energy Management System, developed as part of the Distributed Systems course. This branch introduces the foundational components required to manage users and smart energy devices through a web-based interface.

#### Key Features in `assignment_1`:

- **Frontend (ReactJS)**:
  - Built with ReactJS, providing dedicated interfaces for both administrators and clients.
  - Implements role-based access control (RBAC) with `localStorage` to distinguish between functionalities, ensuring that:
    - **Administrators** can create, read, update, and delete (CRUD) users and devices.
    - **Clients** have read-only access to view their assigned devices.

- **Backend Microservices (Java Spring Boot)**:
  - **User Management Microservice**: Manages user CRUD operations and role assignments (admin/client).
  - **Device Management Microservice**: Handles CRUD operations for smart energy devices and facilitates assigning devices to users.

- **Database (MySQL)**:
  - Each microservice connects to its own MySQL database for persistent storage.
  - User data and device data are kept separate to maintain modular, service-specific storage.

- **REST API**:
  - RESTful APIs facilitate communication between the microservices.

- **Docker Support**:
  - Each component (frontend, backend microservices, and databases) is containerized using Docker, ensuring a consistent environment for deployment and testing.
  - Docker Compose is included to orchestrate the multi-container setup, allowing the entire system to run as a unified, connected application.

