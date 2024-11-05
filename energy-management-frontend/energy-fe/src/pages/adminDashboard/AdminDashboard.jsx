import React from 'react';
import { useNavigate } from 'react-router-dom';
import './AdminDashboard.css';

function AdminDashboard() {
    const navigate = useNavigate();

    // Check local storage for admin status
    const isAdmin = localStorage.getItem('isAdmin') === 'true';

    const handleLogout = () => {
        localStorage.clear();
        navigate('/login');
    };

    const handleManageUsers = () => {
        navigate('/users');
    };

    const handleManageDevices = () => {
        navigate('/devices');
    };
    if (!isAdmin) {
        return (
            <div className="access-denied-container">
                <h1>Access Denied</h1>
                <p>You do not have permission to view this page.</p>
            </div>
        );
    }

    return (
        <div className="dashboard-container">
            <button className="logout-button" onClick={handleLogout}>Logout</button>
            <div className="dashboard-content">
                <h1>Hi, admin!</h1>
                <div className="button-group">
                    <button onClick={handleManageUsers}>Manage Users</button>
                    <button onClick={handleManageDevices}>Manage Devices</button>
                </div>
            </div>
        </div>
    );
}

export default AdminDashboard;
