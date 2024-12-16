import React, { useState, useEffect } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { v4 as uuidv4 } from "uuid";
import { MessageBox, SystemMessage } from "react-chat-elements";
import "react-chat-elements/dist/main.css";
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
    const [notifications, setNotifications] = useState([]);

    useEffect(() => {
        console.log("Initializing WebSocket connection...");

        if (!senderId || !receiverId || !senderUsername || !receiverUsername) {
            console.warn("Missing required query parameters.");
            setMissingParams(true);
            return;
        }

        const socket = new SockJS("http://chat-microservice-spring.localhost/chat");
        const client = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            debug: (str) => console.log(str),
            onConnect: () => setupSubscriptions(client),
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

    const setupSubscriptions = (client) => {
        console.log("Subscribing to topics...");
        client.subscribe(`/topic/chat/${senderId}/${receiverId}`, handleMessage);
        client.subscribe(`/topic/chat/${receiverId}/${senderId}`, handleMessage);
        client.subscribe(`/topic/typing/${receiverId}/${senderId}`, handleTypingStatus);
        client.subscribe(`/topic/notification/${senderId}`, handleNotification);
    };

    const handleMessage = (message) => {
        const msg = JSON.parse(message.body);
        console.log("handleMessage: New message received:", msg);

        setMessages((prev) => {
            if (prev.some((m) => m.messageId === msg.messageId)) {
                console.log("Duplicate message detected. Skipping...");
                return prev;  // Avoid duplicates
            }
            return [...prev, msg];
        });
    };

    const handleTypingStatus = (message) => {
        const typingMsg = JSON.parse(message.body);
        console.log("handleTypingStatus: Typing status received:", typingMsg);

        if (typingMsg.senderId !== senderId) {
            setTypingStatus(`${typingMsg.senderUsername} is typing...`);
            setTimeout(() => setTypingStatus(""), 1500);
        }
    };

    const handleNotification = (message) => {
        const notification = JSON.parse(message.body);
        console.log("handleNotification: Notification received:", notification);

        setNotifications((prev) => [
            ...prev,
            `${notification.senderUsername} sent you a new message.`,
        ]);
        setTimeout(() => setNotifications((prev) => prev.slice(1)), 3000);
    };

    const sendMessage = () => {
        if (!newMessage.trim()) return;
        console.log("sendMessage: Sending new message:", newMessage);

        const message = {
            messageId: uuidv4(),
            senderId,
            senderUsername,
            receiverId,
            receiverUsername,
            message: newMessage,
            timestamp: new Date().toISOString(),
            read: false,
        };

        stompClient.publish({
            destination: "/app/publish",
            body: JSON.stringify(message),
        });

        setMessages((prev) => [
            ...prev,
            { ...message, read: true, timestamp: new Date().toISOString(), self: true },
        ]);
        setNewMessage("");
        sendTypingStatus(false);
    };


    const sendTypingStatus = (isTyping) => {
        console.log("sendTypingStatus: Sending typing status:", isTyping);
        stompClient.publish({
            destination: "/app/typing",
            body: JSON.stringify({ senderId, senderUsername, receiverId, receiverUsername, isTyping }),
        });
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
                        <MessageBox
                            key={msg.messageId}
                            position={msg.self ? "right" : "left"}
                            type="text"
                            text={msg.message}
                            date={new Date(msg.timestamp).toString()}
                            status={msg.read ? "read" : "received"}
                        />
                    ))}
                    {typingStatus && <SystemMessage text={typingStatus} />}
                </div>
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
