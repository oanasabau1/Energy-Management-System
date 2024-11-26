import React, { useState, useEffect } from 'react';
import './Login.css';
import { FaRegUser } from "react-icons/fa";
import { RiLockPasswordLine } from "react-icons/ri";
import { useNavigate } from 'react-router-dom';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    // Clear local storage when the component mounts
    useEffect(() => {
        localStorage.clear(); // Clear any existing user data
    }, []);

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://user-microservice-spring.localhost/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username,
                    password,
                }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Login failed');
            }

            const data = await response.json();
            console.log(data);

            // Store user information in local storage
            localStorage.setItem('userId', data.userId); // Assuming userId is returned from your API
            localStorage.setItem('isAdmin', data.isAdmin); // Assuming isAdmin is a boolean

            // Automatic navigation based on user role
            if (data.isAdmin) {
                navigate('/admin-dashboard');
            } else {
                navigate(`/user/${data.userId}/devices`); // Non-admin user navigation
            }
        } catch (error) {
            console.error('Error:', error.message);
            setErrorMessage(error.message);
        }
    };

    return (
        <div className='wrapper'>
            <form onSubmit={handleLogin}>
                <h1>Login</h1>
                <div className="input-box">
                    <input
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                    <FaRegUser className='icon' />
                </div>
                <div className="input-box">
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    <RiLockPasswordLine className='icon' />
                </div>
                <div className="error-message">{errorMessage}</div>
                <button className="login-btn" type="submit">Login</button>
            </form>
        </div>
    );
};

export default Login;
