package org.example.ai_mha.AiService;

import org.example.ai_mha.DTO.command.ConsultationSessionCreateDTO;
import org.example.ai_mha.entity.ConsultationSession;
import org.example.ai_mha.service.ConsultationMessageService;
import org.example.ai_mha.service.ConsultationSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class PsychologicalSupportService {
    @Autowired
    private ConsultationSessionService consultationSessionService;

    @Autowired
    private ConsultationMessageService consultationMessageService;

    public StructOutPut.StreamChatSession startSession(Long userId, ConsultationSessionCreateDTO createDTO){
        //创建数据库会话记录
        ConsultationSession consultationSession = consultationSessionService.createSession(userId, createDTO);

        //将初始用户消息保存到message表
        consultationMessageService.saveUserMassage(consultationSession.getId(), createDTO.getInitialMessage(),null);

        //创建会话信息
        String sessionId = "session_"+ consultationSession.getId();
        return new StructOutPut.StreamChatSession(
                sessionId,
                userId,
                createDTO.getInitialMessage(),
                System.currentTimeMillis(),
                System.currentTimeMillis()+ 86400000L, //24小时
                 1,
                "ACTIVE"
        );

    }
    public Flux<String> streamPsychologicalChat(String sessionId,String userMessage){
        //创建响应流
        return Flux.create(sink-> {
            //sink.next
        }

        );
    }
}
