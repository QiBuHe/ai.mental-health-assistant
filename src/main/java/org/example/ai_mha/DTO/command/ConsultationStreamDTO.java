package org.example.ai_mha.DTO.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConsultationStreamDTO {
    @NotBlank(message = "session不能为空")
    private String sessionId;
    @NotBlank(message = "初始消息不能为空")
    @Size(max=200,message = "初始消息长度不能超过200")
    private String userMessage;

}
