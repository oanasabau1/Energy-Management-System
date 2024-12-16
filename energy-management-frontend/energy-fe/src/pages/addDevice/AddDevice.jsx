import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './AddDevice.css'; // Import your CSS file for styling

function AddDevice(props) {
    const navigate = useNavigate();

    // Check if the user is an admin
    const isAdmin = localStorage.getItem('isAdmin') === 'true';

    const [userId, setUserId] = useState('');
    const [description, setDescription] = useState('');
    const [address, setAddress] = useState('');
    const [maxHourlyConsumption, setMaxHourlyConsumption] = useState('');
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    // If the user is not an admin, show access denied message
    if (!isAdmin) {
        return (
            <div className="access-denied-container">
                <h1>Access Denied</h1>
                <p>You do not have permission to view this page.</p>
            </div>
        );
    }

    const handleAddDevice = async (e) => {
        e.preventDefault();
        setLoading(true);

        const deviceData = {
            userId: parseInt(userId),
            description,
            address,
            maxHourlyConsumption: parseFloat(maxHourlyConsumption)
        };

        try {
            const response = await fetch('http://device-microservice-spring.localhost/device', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.getItem('jwtToken'),
                },
                body: JSON.stringify(deviceData),
            });

            if (!response.ok) {
                throw new Error('Failed to register device');
            }

            const registeredDevice = await response.json();
            console.log('Registered Device:', registeredDevice);
            navigate('/devices');
        } catch (err) {
            console.error('Error adding device:', err);
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="add-device-container">
            <h2>Add Device</h2>
            {error && <div className="error-message">{error}</div>}
            <form onSubmit={handleAddDevice} className="add-device-form">
                <label htmlFor="userId">User ID:</label>
                <input
                    type="number"
                    id="userId"
                    value={userId}
                    onChange={(e) => setUserId(e.target.value)}
                    required
                    placeholder="Enter User ID"
                />

                <label htmlFor="description">Description:</label>
                <input
                    type="text"
                    id="description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    required
                    placeholder="Enter Device Description"
                />

                <label htmlFor="address">Address:</label>
                <input
                    type="text"
                    id="address"
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                    required
                    placeholder="Enter Address"
                />

                <label htmlFor="maxHourlyConsumption">Max Hourly Consumption (kWh):</label>
                <input
                    type="number"
                    step="0.01" // Allow decimal values
                    id="maxHourlyConsumption"
                    value={maxHourlyConsumption}
                    onChange={(e) => setMaxHourlyConsumption(e.target.value)}
                    required
                    placeholder="Enter Max Hourly Consumption"
                />

                <button type="submit" disabled={loading}>
                    {loading ? 'Adding...' : 'Add Device'}
                </button>
            </form>
        </div>
    );
}

export default AddDevice;
