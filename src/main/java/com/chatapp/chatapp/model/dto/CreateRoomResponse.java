package com.chatapp.chatapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateRoomResponse {
    private String message;
    private String roomId;
    private String status;
}
