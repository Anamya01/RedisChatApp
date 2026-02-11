package com.chatapp.chatapp.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendMessageRequest {
    @NotBlank
    private String participant;
    @NotBlank
    private String message;
}
