package org.example.ai_mha.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.ai_mha.DTO.command.UserLoginCommandDTO;
import org.example.ai_mha.DTO.command.UserRegisterCommandDTO;
import org.example.ai_mha.DTO.response.UserLoginResponseDTO;
import org.example.ai_mha.common.Result;
import org.example.ai_mha.enumClass.UserType;
import org.example.ai_mha.mapper.UserMapper;
import org.example.ai_mha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
//用户登录接口
public class User {
    @Resource
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
        public Result<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginCommandDTO commandDTO){
        UserLoginResponseDTO result = userService.login(commandDTO);
        return Result.success(result);
    }
//用户注册接口
    @PostMapping("/add")
    public Result<UserLoginResponseDTO.UserDetailResponseDTO> register(@Valid @RequestBody UserRegisterCommandDTO commandDTO){


        return null;
    }

}
