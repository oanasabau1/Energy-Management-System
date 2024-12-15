import React, { useState, useEffect } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { v4 as uuidv4 } from "uuid";
import "./Chat.css";

function Chat() {
    const queryParams = new URLSearchParams(window.location.search);
    const senderId = queryParams.get("senderId");
    const receiverId = queryParams.get("receiverId");
    const senderUsername = queryParams.get("senderUsername");
    const receiverUsername = queryParams.get("receiverUsername");

    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState("");
    const [typingStatus, setTypingStatus] = useState("");
    const [missingParams, setMissingParams] = useState(false);
    const [stompClient, setStompClient] = useState(null);

    useEffect(() => {
        if (!senderId || !receiverId || !senderUsername || !receiverUsername) {
            console.warn("Missing required query parameters.");
            setMissingParams(true);
            return;
        }

        console.log("Initializing WebSocket connection...");
        const socketUrl = "http://chat-microservice-spring.localhost/chat";
        const socket = new SockJS(socketUrl);

        const client = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                console.log("WebSocket connected.");

                // Subscribe to messages
                client.subscribe(`/topic/chat/${senderId}/${receiverId}`, handleMessage);
                client.subscribe(`/topic/chat/${receiverId}/${senderId}`, handleMessage);

                // Subscribe to typing status
                client.subscribe(`/topic/typing/${receiverId}/${senderId}`, handleTypingStatus);

                // Subscribe to read receipts
                client.subscribe(`/topic/read/${senderId}`, handleReadReceipt);

                // Mark all messages as read after connecting
                markMessagesAsRead();
            },
            onDisconnect: () => console.log("WebSocket disconnected."),
            onStompError: (frame) => console.error("WebSocket error: ", frame),
        });

        client.activate();
        setStompClient(client);

        return () => {
            console.log("Cleaning up WebSocket subscriptions...");
            client.deactivate();
        };
    }, [senderId, receiverId, senderUsername, receiverUsername]);

    // Handle incoming messages
    const handleMessage = (message) => {
        const msg = JSON.parse(message.body);
        console.log("New message received: ", msg);
        console.log("The message was read by the receiver!");

        setMessages((prev) => {
            if (prev.some((m) => m.messageId === msg.messageId)) {
                console.log("Duplicate message detected. Skipping...");
                return prev; // Prevent duplicates
            }
            return [...prev, msg];
        });

        // Mark the message as read if it's sent by the other user
        if (msg.senderId === receiverId) {
            markMessageAsRead(msg.messageId);
        }
    };

    // Handle typing status
    const handleTypingStatus = (message) => {
        const typingMsg = JSON.parse(message.body);
        console.log("Typing status received: ", typingMsg);

        if (typingMsg.senderId !== senderId) {
            setTypingStatus(`${typingMsg.senderUsername} is typing...`);
            setTimeout(() => setTypingStatus(""), 1500);
        }
    };

    // Handle read receipts
    const handleReadReceipt = (message) => {
        const receipt = JSON.parse(message.body);
        console.log("Read receipt received: ", receipt);

        // Update the read status of the message
        setMessages((prev) =>
            prev.map((msg) =>
                msg.messageId === receipt.messageId ? { ...msg, read: true } : msg
            )
        );
    };

    // Send new message
    const sendMessage = () => {
        if (!newMessage.trim()) return;
        if (!stompClient?.connected) {
            console.error("WebSocket is not connected.");
            return;
        }

        const message = {
            messageId: uuidv4(),
            senderId,
            senderUsername,
            receiverId,
            receiverUsername,
            message: newMessage,
            timestamp: new Date().toISOString(),
        };

        console.log("Sending message: ", message);

        stompClient.publish({
            destination: "/app/publish",
            body: JSON.stringify(message),
        });

        setMessages((prev) => [...prev, { ...message, self: true }]);
        setNewMessage("");
        sendTypingStatus(false);
    };

    // Send typing status
    const sendTypingStatus = (isTyping) => {
        if (!stompClient?.connected) return;

        const typingMessage = { senderId, senderUsername, receiverId, receiverUsername, isTyping };
        console.log("Sending typing status: ", typingMessage);

        stompClient.publish({
            destination: "/app/typing",
            body: JSON.stringify(typingMessage),
        });
    };

    // Mark a single message as read
    const markMessageAsRead = (messageId) => {
        if (!stompClient?.connected) return;

        const readReceipt = {
            messageId,
            senderId,
            receiverId,
        };
        console.log("Sending read receipt: ", readReceipt);

        stompClient.publish({
            destination: "/app/read",
            body: JSON.stringify(readReceipt),
        });
    };

    // Mark all unread messages as read
    const markMessagesAsRead = () => {
        if (!stompClient?.connected) return;

        const unreadMessages = messages.filter(
            (msg) => msg.senderId === receiverId && !msg.read
        );

        unreadMessages.forEach((msg) => markMessageAsRead(msg.messageId));
    };

    if (missingParams) {
        return (
            <div className="access-denied-container">
                <h1>Access Denied</h1>
                <p>You do not have permission to view this page.</p>
            </div>
        );
    }

    return (
        <div className="chat-container">
            <div className="chat-box">
                <h2>Chat with {receiverUsername}</h2>
                <div className="messages">
                    {messages.map((msg) => (
                        <div
                            key={msg.messageId}
                            className={`message ${
                                msg.senderId === senderId || msg.self ? "self" : "other"
                            }`}
                        >
                            <div className="message-content">
                                <strong>
                                    {msg.senderId === senderId || msg.self ? "You" : msg.senderUsername}:
                                </strong>
                                <span>{msg.message}</span>
                                {msg.read && <span className="read-status">✔ Read</span>}
                            </div>
                        </div>
                    ))}
                </div>
                {typingStatus && <div className="typing-status">{typingStatus}</div>}
            </div>

            <div className="chat-input">
                <input
                    type="text"
                    value={newMessage}
                    placeholder="Type your message..."
                    onChange={(e) => {
                        setNewMessage(e.target.value);
                        sendTypingStatus(e.target.value.trim().length > 0);
                    }}
                    onKeyPress={(e) => e.key === "Enter" && sendMessage()}
                />
                <button onClick={sendMessage}>Send</button>
            </div>
        </div>
    );
}

export default Chat;
