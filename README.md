# Energy Management System - Asynchronous Communication and Real-Time Notification

The **Energy Management System** introduces a new **Monitoring and Communication Microservice** to provide real-time energy monitoring and notification capabilities. It integrates seamlessly with existing components and introduces enhancements such as real-time notifications, historical data visualization, and event-driven synchronization.

### Key Features
1. **Energy Data Processing**  
   - Receives energy measurements from RabbitMQ.  
   - Aggregates 10-minute intervals into hourly totals.  
   - Stores aggregated data in a newly introduced MySQL database.  
   - Monitors energy consumption for threshold breaches.  

2. **Real-Time Notifications**  
   - Sends WebSocket notifications to users when hourly energy consumption exceeds the defined limit.  

3. **Historical Data Visualization**  
   - Displays energy consumption through line or bar charts.  
   - Allows users to select specific days for detailed analysis.  

4. **Smart Metering Device Simulator**  
   - Reads energy data from `sensor.csv` and sends measurements to RabbitMQ in JSON format every 10 minutes.  
   - Configurable via `config.properties`, supporting multiple instances with unique device IDs for real-time monitoring across devices.  

5. **Event-Based Synchronization**  
   - Reflects updates in the Device Management Microservice within the Monitoring Microservice.  
   - Delivers notifications asynchronously to clients when thresholds are exceeded.



### Technologies Used
- **Message Broker:** RabbitMQ for asynchronous communication.  
- **Notification System:** WebSocket for real-time client updates.  
- **Backend:** Spring Boot for microservice implementation.  
- **Frontend:** React.js for user interface development.  
- **Database:** MySQL for storing about measurement values, hourly consumption, info about devices.  
- **Load Balancing and Reverse Proxy:** Traefik for efficient traffic routing and scalability.  

![Screenshot 2024-11-25 142028](https://github.com/user-attachments/assets/60ea6495-a34d-45b7-8443-2403998a6622)
![Screenshot 2024-11-25 033432](https://github.com/user-attachments/assets/356715d8-aeb9-4c30-9e2f-0df011f6493d)
![Screenshot 2024-11-25 000407](https://github.com/user-attachments/assets/8ab59527-0385-4dc3-9298-844ceedd813e)
![Screenshot 2024-11-25 000352](https://github.com/user-attachments/assets/ce731f50-356a-4ec0-bc03-75c9bf875458)
![Screenshot 2024-11-24 232947](https://github.com/user-attachments/assets/58b15dce-1533-4853-85af-c58dbc66d70c)
![Screenshot 2024-11-24 221547](https://github.com/user-attachments/assets/40f4e39c-2780-4d23-ab1e-fb0e569a91bb)


