package com.chatapp.chatapp.subscriber;

import org.springframework.stereotype.Component;

@Component
public class RedisMessageSubscriber {

    public void handleMessage(String message) {
        System.out.println("Received message from channel: " + message);
    }
}
