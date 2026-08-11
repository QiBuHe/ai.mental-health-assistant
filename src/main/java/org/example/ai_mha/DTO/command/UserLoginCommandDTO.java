package org.example.ai_mha.DTO.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginCommandDTO {
    @NotBlank(message = "用户名或邮箱不能为空")
    @Size(max=20,message = "用户名或邮箱长度不能超过20")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Size(max=20,min=6,message = "密码长度为6到20")
    private String password;
}
