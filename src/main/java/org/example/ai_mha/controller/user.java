package org.example.ai_mha.controller;

import jakarta.validation.Valid;
import org.example.ai_mha.DTO.command.UserloginCommandDTO;
import org.example.ai_mha.common.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class user {
    @PostMapping("/login")
        public Result<String> login(@Valid @RequestBody UserloginCommandDTO commandDTO){

        System.out.println("username = " + commandDTO.getUsername());
        System.out.println("password = " + commandDTO.getPassword());
        return null;
    }
}
