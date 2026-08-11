package org.example.ai_mha.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.ai_mha.DTO.command.UserLoginCommandDTO;
import org.example.ai_mha.DTO.response.UserLoginResponseDTO;
import org.example.ai_mha.common.Result;
import org.example.ai_mha.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")

public class user {
    @Resource
    private UserService userService;
    @PostMapping("/login")
        public Result<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginCommandDTO commandDTO){

        System.out.println("username = " + commandDTO.getUsername());
        System.out.println("password = " + commandDTO.getPassword());

        Result<UserLoginResponseDTO> result = userService.login(commandDTO);
        System.out.println(result);
        return result;
    }
}
