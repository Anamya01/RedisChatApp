package com.chatapp.chatapp.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRoomRequest {
    @NotBlank(message = "room name cannot be empty")
    private String roomname;
}
