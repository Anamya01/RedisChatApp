package com.chatapp.chatapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateRoomException.class)
    public ResponseEntity<?> handleDuplicate(DuplicateRoomException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorResponse(ex.getMessage()));
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<?> handleNotFound(RoomNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse("Internal server error"));
    }

    private Map<String, Object> errorResponse(String message) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "message", message,
                "status", "error"
        );
    }
}
