import { useState, useEffect } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const useWebSocket = (topic, onMessageReceived) => {
    const [stompClient, setStompClient] = useState(null);

    useEffect(() => {
        const client = new Client({
            webSocketFactory: () => new SockJS('/ws'),
            onConnect: () => {
                console.log('WebSocket Connected!');
                client.subscribe(topic, (message) => {
                    onMessageReceived(JSON.parse(message.body));
                });
            },
            onStompError: (frame) => {
                console.error('Broker reported error: ' + frame.headers['message']);
                console.error('Additional details: ' + frame.body);
            },
        });

        client.activate();
        setStompClient(client);

        return () => {
            if (client) {
                client.deactivate();
                console.log('WebSocket Disconnected.');
            }
        };
    }, [topic, onMessageReceived]);

    return stompClient;
};

export default useWebSocket;
