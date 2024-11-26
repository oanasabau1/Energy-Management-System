import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';
import { DatePicker } from 'antd';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import dayjs from 'dayjs';
import './DevicePage.css';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

function DevicePage() {
  const { id } = useParams();
  const [device, setDevice] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [energyData, setEnergyData] = useState([]);
  const [groupedData, setGroupedData] = useState([]);
  const [selectedDate, setSelectedDate] = useState(null);
  const [notifications, setNotifications] = useState([]);

  // Fetch device details
  useEffect(() => {
    const fetchDeviceDetails = async () => {
      try {
        const response = await fetch(`http://device-microservice-spring.localhost/device/${id}`);
        if (!response.ok) throw new Error('Failed to fetch device details');
        const data = await response.json();
        setDevice(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchDeviceDetails();
  }, [id]);

  // WebSocket setup for notifications
  useEffect(() => {
    const socket = new SockJS('http://monitoring-microservice-spring.localhost/monitoring');
    const client = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        console.log('Connected to WebSocket');
        client.subscribe(`/topic/notifications/${id}`, (message) => {
          const notification = JSON.parse(message.body);
          setNotifications([notification]); // Keep only the latest notification
        });
      },
      onDisconnect: () => {
        console.log('Disconnected from WebSocket');
      },
      debug: (str) => {
        console.log(str);
      },
    });
    client.activate();

    return () => {
      if (client) client.deactivate();
    };
  }, [id]);

  // Fetch energy data
  const fetchEnergyData = async (date) => {
    try {
      const response = await fetch(`http://monitoring-microservice-spring.localhost/device/${id}/measurements/${date}`);
      if (!response.ok) throw new Error('Failed to fetch energy data');
      const data = await response.json();
      console.log(data);
      setEnergyData(data);
      groupDataByHour(data);
    } catch (err) {
      console.error('Error fetching energy data:', err);
      setEnergyData([]);
      setGroupedData([]);
    }
  };

  // Group data by hour
  const groupDataByHour = (data) => {
    const grouped = Array.from({ length: 24 }, (_, hour) => ({
      hour,
      consumption: 0,
    }));

    data.forEach((measurement) => {
      const hour = dayjs(measurement.timestamp).hour(); // Extract hour
      if (!isNaN(measurement.value)) {
        grouped[hour].consumption += measurement.value;
      }
    });

    // Round consumption to 2 decimal places
    grouped.forEach((item) => {
      item.consumption = Math.round(item.consumption * 100) / 100;
    });

    console.log('Grouped Data by Hour:', grouped);
    setGroupedData(grouped);
  };

  const handleDateChange = (date, dateString) => {
    setSelectedDate(dateString);
    fetchEnergyData(dateString);
  };

  // Chart configuration
  const chartData = {
    labels: groupedData.map((item) => `${item.hour}:00`),
    datasets: [
      {
        label: 'Energy Consumption (kWh)',
        data: groupedData.map((item) => item.consumption),
        backgroundColor: 'rgba(34,139,34,0.5)',
        borderColor: 'rgba(34,139,34,1)',
        borderWidth: 1,
        tension: 0.3,
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    plugins: {
      title: {
        display: true,
        text: `Energy Consumption for ${selectedDate || 'Selected Date'}`,
      },
    },
    scales: {
      x: { title: { display: true, text: 'Hour' } },
      y: { title: { display: true, text: 'Energy Consumption (kWh)' } },
    },
  };

  if (loading) return <p>Loading device details...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div className="device-details">
      <h1>Device Details</h1>
      {device ? (
        <div>
          <p><strong>Description:</strong> {device.description}</p>
          <p><strong>Address:</strong> {device.address}</p>
          <p><strong>Max Hourly Consumption:</strong> {device.maxHourlyConsumption} kWh</p>
        </div>
      ) : (
        <p>No device data available</p>
      )}

      <div className="chart-container">
        <h2>Select Date</h2>
        <DatePicker className="date-picker" onChange={handleDateChange} />

        {groupedData.length > 0 ? (
          <div>
            <h2>Energy Consumption Chart</h2>
            <Line data={chartData} options={chartOptions} />
          </div>
        ) : (
          <p>No energy data available for the selected date.</p>
        )}
      </div>

      <div className="notification-container">
        <h2>Latest Notification</h2>
        {notifications.length === 0 ? (
          <p>No notifications yet</p>
        ) : (
          <div className="notification">
            <p><strong>Device with ID {notifications[0].deviceId}</strong> exceeded max hourly consumption.</p>
            <p><strong>The value measured is:</strong> {notifications[0].totalConsumption} kWh</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default DevicePage;
