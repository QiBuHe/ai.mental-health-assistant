package org.example.ai_mha.AiService;

public class StructOutPut {
    public record StreamChatSession(
            String SessionId,
            Long userHash,
            String initialMessage,
            Long startTime,
            long expiryTime,
            Integer messageCount,
            String status
    ){

    }
}
