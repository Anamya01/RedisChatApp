package com.chatapp.chatapp.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JoinRoomRequest {
    @NotBlank(message = "Participant name cannot be empty")
    private String participants;
}
