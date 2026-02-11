package com.chatapp.chatapp.service;


import com.chatapp.chatapp.exception.DuplicateRoomException;
import com.chatapp.chatapp.exception.RoomNotFoundException;
import com.chatapp.chatapp.model.ChatMessage;
import com.chatapp.chatapp.repository.RedisChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final RedisChatRepository repository;
    private final ObjectMapper objectMapper;

    public void createRoom(String roomId) {

        Map<String, String> metadata = new HashMap<>();
        metadata.put("roomId", roomId);
        metadata.put("createdAt", Instant.now().toString());

        boolean created = repository.createRoom(roomId, metadata);

        if (!created) {
            throw new DuplicateRoomException(roomId);
        }
    }


    public void joinRoom(String roomId, String participant) {

        if (!repository.roomExists(roomId)) {
            throw new RoomNotFoundException(roomId);
        }

        repository.addParticipant(roomId, participant);
    }


    public void sendMessage(String roomId, String participant, String message) {

        if (!repository.roomExists(roomId)) {
            throw new RoomNotFoundException(roomId);
        }

        ChatMessage chatMessage = ChatMessage.builder()
                .participant(participant)
                .message(message)
                .timestamp(Instant.now().toString())
                .build();

        String messageJson = serialize(chatMessage);

        repository.saveMessage(roomId, messageJson);
    }


    public List<ChatMessage> getLastMessages(String roomId, int limit) {

        if (!repository.roomExists(roomId)) {
            throw new RoomNotFoundException(roomId);
        }

        List<String> rawMessages = repository.getLastMessages(roomId, limit);
        if(rawMessages == null){
            return List.of();
        }
        return rawMessages.stream().map(this::deserialize).toList();
    }

    private ChatMessage deserialize(String json) {
        try {
            return objectMapper.readValue(json, ChatMessage.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize message", e);
        }
    }

    private String serialize(ChatMessage message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize message", e);
        }
    }
}

