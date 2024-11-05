import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import './UpdateDevice.css';

function UpdateDevice() {
    const navigate = useNavigate();
    const { deviceId } = useParams();

    const [description, setDescription] = useState('');
    const [address, setAddress] = useState('');
    const [maxHourlyConsumption, setMaxHourlyConsumption] = useState('');
    const [userId, setUserId] = useState(1); // hardcoded userId to 1 (as admin) because it is requested in the payload, but doen't affect anything
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const [isAdmin, setIsAdmin] = useState(null);

    useEffect(() => {
        const adminStatus = localStorage.getItem('isAdmin') === 'true';
        setIsAdmin(adminStatus);
    }, []);

    const handleUpdateDevice = async (e) => {
        e.preventDefault();
        setLoading(true);

        const updatedDeviceData = {
            userId,
            description,
            address,
            maxHourlyConsumption: parseFloat(maxHourlyConsumption),
        };

        try {
            const response = await fetch(`http://localhost:8081/device/update/${deviceId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(updatedDeviceData),
            });

            if (!response.ok) {
                throw new Error('Failed to update device');
            }

            const updatedDevice = await response.json();
            console.log('Updated Device:', updatedDevice);
            navigate('/devices');
        } catch (err) {
            console.error('Error updating device:', err);
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

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
        <div className="update-device-container">
            <h2>Update Device</h2>
            {error && <div className="error-message">{error}</div>}
            <form onSubmit={handleUpdateDevice} className="update-device-form">
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
                    step="0.1"
                    id="maxHourlyConsumption"
                    value={maxHourlyConsumption}
                    onChange={(e) => setMaxHourlyConsumption(e.target.value)}
                    required
                    placeholder="Enter Max Hourly Consumption"
                />

                <button type="submit" disabled={loading}>
                    {loading ? 'Updating...' : 'Update Device'}
                </button>
            </form>
        </div>
    );
}

export default UpdateDevice;
