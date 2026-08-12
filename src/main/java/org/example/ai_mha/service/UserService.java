package org.example.ai_mha.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.example.ai_mha.DTO.command.UserLoginCommandDTO;
import org.example.ai_mha.DTO.command.UserRegisterCommandDTO;
import org.example.ai_mha.DTO.response.UserLoginResponseDTO;
import org.example.ai_mha.entity.User;
import org.example.ai_mha.enumClass.UserType;
import org.example.ai_mha.exception.BusinessException;
import org.example.ai_mha.mapper.UserMapper;
import org.example.ai_mha.service.convert.UserConvert;
import org.example.ai_mha.util.JwtTokenUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    public UserLoginResponseDTO login(UserLoginCommandDTO commandDTO) {
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
        UserLoginResponseDTO.UserDetailResponseDTO userInfo = UserConvert.entityToDetailResponse(user);
        return UserConvert.entityToLoginResponse(token,userInfo);
    }

    //创建用户

        public UserLoginResponseDTO.UserDetailResponseDTO register(UserRegisterCommandDTO commandDTO) {
            System.out.println(JSONUtil.parseObj(commandDTO));
            //验证密码是否一致
            if(!commandDTO.getPassword().equals(commandDTO.getConfirmPassword())){
                throw new RuntimeException("密码不一致");
            }

            //检查用户名是否存在
            LambdaQueryWrapper<org.example.ai_mha.entity.User> userNameQuery = new LambdaQueryWrapper<>();
            userNameQuery.eq(org.example.ai_mha.entity.User::getUsername, commandDTO.getUsername());
            if(userMapper.selectCount(userNameQuery) > 0){
                throw new RuntimeException("用户名已存在");
            }

            //检查邮箱
            LambdaQueryWrapper<org.example.ai_mha.entity.User> emailQuery = new LambdaQueryWrapper<>();
            emailQuery.eq(org.example.ai_mha.entity.User::getEmail, commandDTO.getEmail());
            if(userMapper.selectCount(emailQuery) > 0){
                throw new RuntimeException("邮箱已存在");
            }

            //验证用户类型
            if(!UserType.isValidCode(commandDTO.getUserType())){
                throw new RuntimeException("无效的用户类型");
            }

            //创建用户
            String password = commandDTO.getPassword().trim();
            String encodedPassword = passwordEncoder.encode(password);

            User user = UserConvert.registerCommandToEntity(commandDTO,encodedPassword);
            //插入数据库
            userMapper.insert(user);
            return UserConvert.entityToDetailResponse(user);
        }
        public UserLoginResponseDTO.UserDetailResponseDTO getUserById(Long userid) {
        User user = userMapper.selectById(userid);
            if(user == null){
                throw new BusinessException("用户不存在");
            }
            return UserConvert.entityToDetailResponse(user);
        }
}

