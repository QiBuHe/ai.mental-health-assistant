package org.example.ai_mha.DTO.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegisterCommandDTO {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度在2到20之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @Size(min = 6, max = 50, message = "邮箱长度在6到50之间")
    private String email;

    @NotBlank(message = "昵称不能为空")
    @Size(min = 2, max = 20, message = "昵称长度在2到20之间")
    private String nickname;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度在6到20之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "密码只能包含字母、数字和下划线")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    @Size(min = 6, max = 20, message = "确认密码长度在6到20之间")
    private String confirmPassword;

    private LocalDate birthday;

    private Integer gender;

    private Integer userType=1;

}
