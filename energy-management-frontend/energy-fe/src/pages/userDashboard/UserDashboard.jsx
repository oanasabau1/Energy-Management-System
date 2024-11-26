import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './UserDashboard.css';

function UserDashboard() {
    const navigate = useNavigate();
    const [devices, setDevices] = useState([]);
    const [user, setUser] = useState(null); // New state for user data
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Assuming the user ID is stored in local storage after login
    const userId = localStorage.getItem('userId');

    useEffect(() => {
        const fetchUser = async () => {
            if (userId) {
                try {
                    const response = await fetch(`http://device-microservice-spring.localhost/user/${userId}`);
                    if (!response.ok) {
                        throw new Error('Failed to fetch user');
                    }
                    const userData = await response.json();
                    setUser(userData); // Set user data
                } catch (err) {
                    console.error('Error fetching user:', err);
                    setError(err.message);
                }
            } else {
                setError('User ID not found');
            }
        };

        fetchUser();
    }, [userId]);

    useEffect(() => {
        const fetchDevices = async () => {
            setLoading(true);

            try {
                const response = await fetch(`http://device-microservice-spring.localhost/user/${userId}/devices`);
                if (!response.ok) {
                    throw new Error('Failed to fetch devices');
                }
                const deviceData = await response.json();
                setDevices(deviceData);
            } catch (err) {
                console.error('Error fetching devices:', err);
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        if (userId) {
            fetchDevices();
        } else {
            setError('User ID not found');
            setLoading(false);
        }
    }, [userId]);

    const handleLogout = () => {
        localStorage.clear(); // Clear local storage on logout
        navigate('/login'); // Redirect to the login page
    };

    // Function to navigate to device details
    const handleDeviceDetails = (deviceId) => {
        navigate(`/device/${deviceId}`); // Navigate to the device details page
    };

    return (
        <div>
            <div className="logout-container">
                <button className="logout-button" onClick={handleLogout}>Logout</button>
            </div>
            <div className="user-dashboard-container">
                <h1>Hi, {user ? user.username : 'User'}!</h1>
                <h2>Your Devices</h2>
                {loading && <p>Loading devices...</p>}
                {error && <div className="error-message">{error}</div>}
                {!loading && !error && (
                    <ul className="device-list">
                        {devices.map(device => (
                            <li key={device.deviceId} className="device-item">
                                <div className="device-info">
                                    <span className="device-description">{device.description}</span>
                                    <span className="device-address">{device.address}</span>
                                </div>
                                <span className="device-consumption">{device.maxHourlyConsumption} kWh</span>
                                <button className="btn"
                                    onClick={() => handleDeviceDetails(device.deviceId)}
                                >
                                    View Details
                                </button>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
}

export default UserDashboard;
