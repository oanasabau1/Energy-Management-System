import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './UserManagement.css';

function UserManagement() {
    const navigate = useNavigate();

    // Check local storage for admin status
    const [isAdmin, setIsAdmin] = useState(false);
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Check if the user is an admin when the component mounts
    useEffect(() => {
        const adminStatus = localStorage.getItem('isAdmin') === 'true';
        setIsAdmin(adminStatus);
    }, []);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await fetch('http://user-microservice-spring.localhost/users');
                if (!response.ok) {
                    throw new Error('Failed to fetch users');
                }
                const usersData = await response.json();
                setUsers(usersData);
            } catch (err) {
                console.error('Error fetching users:', err);
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchUsers();
    }, []);

    const handleEdit = (userId) => {
        navigate(`/user/update/${userId}`); // Ensure userId is being passed correctly
    };

    const handleDelete = async (userId) => {
        if (window.confirm('Are you sure you want to delete this user?')) {
            try {
                const response = await fetch(`http://user-microservice-spring.localhost/user/delete/${userId}`, {
                    method: 'DELETE',
                });
                if (!response.ok) {
                    throw new Error('Failed to delete user');
                }
                // Remove the deleted user from the state
                setUsers(users.filter(user => user.userId !== userId)); // Change from user.id to user.userId
                console.log(`User with ID ${userId} deleted`);
            } catch (err) {
                console.error('Error deleting user:', err);
                setError(err.message);
            }
        }
    };

    const handleAddUser = () => {
        navigate('/user');
    };

    const handleLogout = () => {
        localStorage.clear(); // Clear local storage on logout
        navigate('/login');
    };

    if (!isAdmin) {
        return (
            <div className="access-denied-container">
                <h1>Access Denied</h1>
                <p>You do not have permission to view this page.</p>
            </div>
        );
    }

    if (loading) {
        return <div>Loading users...</div>;
    }

    // Filter users who are not admins to be displayed
    const nonAdminUsers = users.filter(user => !user.isAdmin);

    return (
        <div className="user-management-container">
            <button className="logout-button" onClick={handleLogout}>Logout</button>
            <div className="user-management">
                <h1>All Users</h1>
                {error && <div className="error-message">{error}</div>}
                <button className="add-user-button" onClick={handleAddUser}>Add User</button>
                <ul>
                    {nonAdminUsers.map(user => (
                        <li key={user.userId} className="user-item">
                            <span className="user-name">{user.username}</span>
                            <div className="button-group">
                                <button className="edit-button" onClick={() => handleEdit(user.userId)}>Edit</button>
                                <button className="delete-button" onClick={() => handleDelete(user.userId)}>Delete</button>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}

export default UserManagement;
