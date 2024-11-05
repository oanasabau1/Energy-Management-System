import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './DeviceManagement.css';

function DeviceManagement() {
    const navigate = useNavigate();
    const [devices, setDevices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isAdmin, setIsAdmin] = useState(null);

    useEffect(() => {
        const fetchDevices = async () => {
            try {
                const response = await fetch('http://localhost:8081/devices');
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                setDevices(data);
            } catch (error) {
                console.error('Error fetching devices:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchDevices();
    }, []);

    useEffect(() => {
        const adminStatus = localStorage.getItem('isAdmin') === 'true';
        setIsAdmin(adminStatus);
    }, []);

    const handleEdit = (deviceId) => {
        navigate(`/device/update/${deviceId}`);
    };

    const handleDelete = async (deviceId) => {
         if (window.confirm('Are you sure you want to delete this device?')) {
             try {
                 const response = await fetch(`http://localhost:8081/device/delete/${deviceId}`, {
                     method: 'DELETE',
                     headers: {
                         'Content-Type': 'application/json'
                     }
                 });
                 if (!response.ok) {
                     throw new Error('Network response was not ok');
                 }
                 setDevices((prevDevices) => prevDevices.filter(device => device.deviceId !== deviceId));
             } catch (error) {
                 console.error('Error deleting device:', error);
             }
         }
    };

    const handleAddDevice = () => {
        navigate('/device');
    };

    const handleLogout = () => {
        localStorage.clear();
        navigate('/login');
    };

    if (loading) {
        return <div>Loading devices...</div>;
    }

    if (isAdmin === null) {
        return <div>Loading admin status...</div>;
    }

    if (!isAdmin) {
        return (
            <div className="access-denied-container">
                <h1>Access Denied</h1>
                <p>You do not have permission to view this page.</p>
            </div>
        );
    }

    return (
        <div className="device-management-container">
            <button className="logout-button" onClick={handleLogout}>Logout</button>
            <div className="device-management">
                <h1>All Devices</h1>
                <button className="add-device-button" onClick={handleAddDevice}>Add Device</button>
                <ul>
                    {devices.map(device => (
                        <li key={device.deviceId} className="device-item">
                            <span className="device-id">Device ID: {device.deviceId}</span>
                            <span className="user-id">User ID: {device.userId}</span>
                            <span className="device-description">{device.description}</span>
                            <span className="device-address">{device.address}</span>
                            <span className="device-consumption">{device.maxHourlyConsumption} kWh</span>
                            <div className="button-group">
                                <button className="edit-button" onClick={() => handleEdit(device.deviceId)}>Edit</button>
                                <button className="delete-button" onClick={() => handleDelete(device.deviceId)}>Delete</button>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}

export default DeviceManagement;
