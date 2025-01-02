import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './AddUser.css';

function AddUser() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    // Check local storage for admin status
    const isAdmin = localStorage.getItem('isAdmin') === 'true';

    // If the user is not an admin, show access denied message
    if (!isAdmin) {
        return (
            <div className="access-denied-container">
                <h1>Access Denied</h1>
                <p>You do not have permission to view this page.</p>
            </div>
        );
    }

    const handleAddUser = async (e) => {
        e.preventDefault();

        const newUser = {
            username,
            password,
            isAdmin: false // Set isAdmin to false implicitly
        };

        try {
            const response = await fetch('http://user-microservice-spring.localhost/user', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.getItem('jwtToken'),
                },
                body: JSON.stringify(newUser),
            });

            if (!response.ok) {
                throw new Error('Failed to add user');
            }

            const registeredUser = await response.json();
            console.log(`New User Registered: ${registeredUser.username}`);
            navigate('/users');
        } catch (err) {
            console.error('Error adding user:', err);
            setError(err.message);
        }
    };

    return (
        <div className="add-user-wrapper">
            <div className="add-user-container">
                <h2>Add User</h2>
                {error && <div className="error-message">{error}</div>}
                <form onSubmit={handleAddUser} className="add-user-form">
                    <label htmlFor="username">Username:</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                        placeholder="Enter username"
                    />

                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        placeholder="Enter password"
                    />

                    <button type="submit">Add User</button>
                </form>
            </div>
        </div>
    );
}

export default AddUser;
