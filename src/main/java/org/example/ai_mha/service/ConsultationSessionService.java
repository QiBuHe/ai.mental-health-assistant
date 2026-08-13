package org.example.ai_mha.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.example.ai_mha.DTO.command.ConsultationSessionCreateDTO;
import org.example.ai_mha.entity.ConsultationSession;
import org.example.ai_mha.entity.User;
import org.example.ai_mha.mapper.ConsultationSessionMapper;
import org.example.ai_mha.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConsultationSessionService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ConsultationSessionMapper consultationSessionMapper;

    public ConsultationSession createSession(Long userId, ConsultationSessionCreateDTO createDTO){

        //验证用户是否存在
        User user = userMapper.selectById(userId);
        if(user != null){
            //创建会话记录
            ConsultationSession session = ConsultationSession.builder()
                    .userId(userId)
                    .sessionTitle(createDTO.getSeesionTitle())
                    .startedAt(LocalDateTime.now())
                    .build();

            //当前标题没有传
            if(StrUtil.isBlank(createDTO.getSeesionTitle())){
                session.setSessionTitle(String.format(("心理ai助手 - "+ DateUtil.format(LocalDateTime.now(),"MM-dd HH:mm:ss"))));

            }
        //插入记录
            consultationSessionMapper.insert(session);
            return session;
        }

        return null;
    }
}
