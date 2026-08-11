package org.example.ai_mha.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.example.ai_mha.DTO.command.UserLoginCommandDTO;
import org.example.ai_mha.DTO.response.UserLoginResponseDTO;
import org.example.ai_mha.common.Result;
import org.example.ai_mha.entity.User;
import org.example.ai_mha.exception.BusinessException;
import org.example.ai_mha.mapper.UserMapper;
import org.example.ai_mha.util.JwtTokenUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    public Result<UserLoginResponseDTO> login(UserLoginCommandDTO commandDTO) {
        // 构建查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, commandDTO.getUsername())
                .or()
                .eq(User::getEmail, commandDTO.getUsername());
        // 调用MP API查询
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);

        // 判断用户是否存在
        if(user == null) {
           throw new BusinessException("用户不存在");
        }

        //判断密码
        String inputPassword = commandDTO.getPassword().trim();
        if(!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        //检查用户状态
        if(!user.isActive()){
            throw new BusinessException("用户已被禁用,请联系管理员");
        }

        //生成token
        String token = JwtTokenUtil.generateToken(user.getId(), user.getUsername(),user.getUserType());
        System.out.println(token);

        return null;
    }
}

