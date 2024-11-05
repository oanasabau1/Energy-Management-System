import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import './UpdateUser.css';

function UpdateUser() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const [isAdmin, setIsAdmin] = useState(null);
    const navigate = useNavigate();
    const { userId } = useParams(); // Get userId from URL

    // Check local storage for admin status
    useEffect(() => {
        const adminStatus = localStorage.getItem('isAdmin') === 'true';
        setIsAdmin(adminStatus);
    }, []);

    const handleUpdate = async (e) => {
        e.preventDefault();

        const updatedUser = { username, password };

        try {
            const response = await fetch(`http://localhost:8080/user/update/${userId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(updatedUser),
            });

            if (!response.ok) {
                throw new Error('Failed to update user');
            }

            const updatedUserData = await response.json();
            console.log(`User Updated: ${updatedUserData.username}`);
            navigate('/users');
        } catch (err) {
            console.error('Error updating user:', err);
            setError(err.message);
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
        <div className="update-user-container">
            <h2>Update User</h2>
            {error && <div className="error-message">{error}</div>}
            <form onSubmit={handleUpdate} className="update-form">
                <label htmlFor="username">Username:</label>
                <input
                    type="text"
                    id="username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                    placeholder="Enter new username"
                />

                <label htmlFor="password">Password:</label>
                <input
                    type="password"
                    id="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                    placeholder="Enter new password"
                />

                <button type="submit">Update User</button>
            </form>
        </div>
    );
}

export default UpdateUser;
