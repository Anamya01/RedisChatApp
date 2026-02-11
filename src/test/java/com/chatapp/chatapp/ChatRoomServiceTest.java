package com.chatapp.chatapp;

import com.chatapp.chatapp.service.ChatRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatRoomIntegrationTest {

    @Autowired
    private ChatRoomService service;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    void cleanRedis() {
        redisTemplate.getConnectionFactory()
                .getConnection()
                .serverCommands()
                .flushAll();
    }

    @Test
    void createRoom_success() {
        service.createRoom("general");

        assertTrue(
                redisTemplate.opsForSet()
                        .isMember("chatrooms", "general")
        );
    }

    @Test
    void createRoom_duplicate_shouldFail() {
        service.createRoom("general");

        assertThrows(
                RuntimeException.class,
                () -> service.createRoom("general")
        );
    }

    @Test
    void sendMessage_andRetrieveLast() {

        service.createRoom("general");
        service.joinRoom("general", "user1");

        service.sendMessage("general", "user1", "Hello");
        service.sendMessage("general", "user1", "World");

        var messages = service.getLastMessages("general", 1);

        assertEquals(1, messages.size());
        assertEquals("World", messages.get(0).getMessage());
    }

    @Test
    void sendMessage_withoutJoining_shouldFail() {

        service.createRoom("general");

        assertThrows(
                RuntimeException.class,
                () -> service.sendMessage("general", "user1", "Hello")
        );
    }

    @Test
    void deleteRoom_shouldRemoveEverything() {

        service.createRoom("general");
        service.joinRoom("general", "user1");

        service.deleteRoom("general");

        assertFalse(
                redisTemplate.opsForSet()
                        .isMember("chatrooms", "general")
        );
    }
}
