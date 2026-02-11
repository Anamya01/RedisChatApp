package com.chatapp.chatapp.controller;

import com.chatapp.chatapp.model.*;
import com.chatapp.chatapp.model.dto.*;
import com.chatapp.chatapp.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatapp/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService service;

    // Create Room
    @PostMapping
    public CreateRoomResponse createRoom(
            @Valid @RequestBody CreateRoomRequest request) {

        service.createRoom(request.getRoomName());

        return new CreateRoomResponse(
                "Chat room '" + request.getRoomName() + "' created successfully.",
                request.getRoomName(),
                "success"
        );
    }

    // Join Room
    @PostMapping("/{roomId}/join")
    public GenericResponse joinRoom(
            @PathVariable String roomId,
            @Valid @RequestBody JoinRoomRequest request) {

        service.joinRoom(roomId, request.getParticipants());

        return new GenericResponse(
                "User '" + request.getParticipants() +
                        "' joined chat room '" + roomId + "'.",
                "success"
        );
    }

    // Send Message
    @PostMapping("/{roomId}/messages")
    public GenericResponse sendMessage(
            @PathVariable String roomId,
            @Valid @RequestBody SendMessageRequest request) {

        service.sendMessage(
                roomId,
                request.getParticipant(),
                request.getMessage()
        );

        return new GenericResponse(
                "Message sent successfully.",
                "success"
        );
    }

    // Get Messages
    @GetMapping("/{roomId}/messages")
    public ChatHistoryResponse getMessages(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "10") int limit) {

        return new ChatHistoryResponse(
                service.getLastMessages(roomId, limit)
        );
    }
}
