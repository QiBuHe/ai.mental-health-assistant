package org.example.ai_mha.DTO.response;

import lombok.Builder;
import lombok.Data;

@Data
public class UserLoginResponseDTO {
    private String token;
    private String roleType;
    private UserDetailResponseDTO userInfo;
    @Builder

    public static class UserDetailResponseDTO {
        private Long id;
        private String username;
        private String email;
        private String nickname;
        private String phone;
        private Integer gender;
        private String genderDisplayName;
        private Integer userType;
        private String userTypeDisplayName;
        private Integer status;
        private String statusDisplayName;
        private String displayName;
        private String createdAt;
        private String updatedAt;
    }
}

