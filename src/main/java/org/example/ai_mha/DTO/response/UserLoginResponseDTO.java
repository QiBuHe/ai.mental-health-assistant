package org.example.ai_mha.DTO.response;

import lombok.Data;

@Data
public class UserLoginResponseDTO {
    private String token;
    private String roleType;
    private UserDetailResponseDTO userInfo;

    //静态内部类，嵌套在里面
/*       "id": 71,
         "username": "string",
         "email": "string",
         "nickname": "string",
         "phone": "string",
         "gender": 0,
         "genderDisplayName": "未知",
         "userType": 1,
         "userTypeDisplayName": "普通用户",
         "status": 1,
         "statusDisplayName": "正常",
         "displayName": "string",
         "createdAt": "2026-03-06 21:17:50",
         "updatedAt": "2026-03-06 21:17:50"*/
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

