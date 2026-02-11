package com.chatapp.chatapp.model.dto;

import com.chatapp.chatapp.model.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatHistoryResponse {
    private List<ChatMessage> messages;
}
