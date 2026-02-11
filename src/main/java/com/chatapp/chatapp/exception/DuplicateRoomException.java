package com.chatapp.chatapp.exception;

public class DuplicateRoomException extends RuntimeException {
    public DuplicateRoomException(String roomId) {
        super("Chat room '" + roomId + "' already exists");
    }
}
