package org.example.ai_mha.controller;

import cn.hutool.json.JSONUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
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
import org.example.ai_mha.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/User")
//用户登录接口
public class User {
    @Resource
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
        public Result<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginCommandDTO commandDTO){
        UserLoginResponseDTO result = userService.login(commandDTO);
        return Result.success(result);
    }
//用户注册接口
    @PostMapping("/add")
    public Result<UserLoginResponseDTO.UserDetailResponseDTO> register(@Valid @RequestBody UserRegisterCommandDTO commandDTO){
        UserLoginResponseDTO.UserDetailResponseDTO result = userService.register(commandDTO);

        return Result.success(result);
    }
    //获取当前用户
    @GetMapping("/current")
    public Result<UserLoginResponseDTO.UserDetailResponseDTO> getCurrentUser(){
        String token = JwtTokenUtil.getCurrentToken();
        DecodedJWT jwt = JwtTokenUtil.verifyToken(token);
        Long userId = jwt.getClaim("userId").asLong();
        //调用Serice层方法获取用户信息
        UserLoginResponseDTO.UserDetailResponseDTO result = userService.getUserById(userId);

        return Result.success(result);
    }
}
