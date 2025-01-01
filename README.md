# Energy Management System - Real-Time Chat Application

The **Energy Management System** introduces a chat microservice and authorization to provide real-time energy monitoring, notifications, user and device management.

## Key Features  

1. **Real-Time Chat Microservice**  
   - Enables real-time chat functionality between users and the admin using WebSockets.  
   - Admin can chat with multiple users simultaneously, while clients can only communicate with the admin.  
   - Displays typing notifications and read receipts.  

2. **Secure Authorization**  
   - Implements JWT-based authentication to secure each endpoint.  
   - Restricts access to specific system features based on user roles and permissions.  

## Technologies Used  
- **Chat System:** WebSocket for real-time communication.  
- **Backend:** Spring Boot for microservice implementation.  
- **Authentication:** JWT for secure endpoint access.

I've added couple screenshots to prove that the project works as expected: 

![Screenshot 2025-01-02 000740](https://github.com/user-attachments/assets/4890ef17-ea7e-46a4-aed8-6b5272834ac0)
![Screenshot 2025-01-02 000706](https://github.com/user-attachments/assets/484efc81-c6d4-4a53-b521-0230ddee2ec9)
