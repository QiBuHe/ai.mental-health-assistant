package org.example.ai_mha.AiService;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.example.ai_mha.DTO.command.ConsultationSessionCreateDTO;
import org.example.ai_mha.DTO.response.ConsultationMessageResponseDTO;
import org.example.ai_mha.entity.ConsultationSession;
import org.example.ai_mha.service.ConsultationMessageService;
import org.example.ai_mha.service.ConsultationSessionService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
public class PsychologicalSupportService {

    @Autowired
    @Qualifier("open-ai")
    private ChatClient chatClient;

    @Autowired
    private ChatMemory chatMemory;

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
            //sink.next("数据1“）发布数据
            //sink.complete()完成流
            //sink.error()发送错误
            Long dbSeeionId = extractSessional(sessionId);
            if(dbSeeionId == null){
                sink.error(new RuntimeException("无效的会话ID"));
                return;
            }
            //是否为初始消息
            boolean isInitialMessage = false;
            //检查是否为初始消息，避免重复保存
            Integer messageCount = consultationMessageService.getMessageCountBySession(dbSeeionId);
            if(messageCount == 1){
                ConsultationMessageResponseDTO lastMessage = consultationMessageService.getLastMessageBySessionId(dbSeeionId);

                if(lastMessage !=null && lastMessage.getSenderType() == 1 && userMessage.equals(lastMessage.getContent())){
                    isInitialMessage = true;
                }
            }
            if(!isInitialMessage){
                //保存用户消息到数据库
                consultationMessageService.saveUserMassage(dbSeeionId,userMessage,null);

            }
            //进行流式对话
            //生成记忆体
            String conversationId = "conversation_"+sessionId;
            //构建系统提示词
            List<Message> userMessages = new ArrayList<>();
            userMessages.add(new UserMessage(userMessage));
            chatMemory.add(conversationId,userMessages);
            Prompt prompt = new Prompt(List.of(
                    new SystemMessage(PromptManage.PSYCHOLOGICAL_SUPPORT_SYSTEM_PROMPT)
            ));

            //用于存储ai完整相应
            StringBuilder fullResponse = new StringBuilder();
            //使用它发送消息到ai
            chatClient.prompt(prompt)
                    .user(userMessage)
                    .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,conversationId))
                    .stream()
                    .content()
                    .doOnNext(Fragment -> {
                        fullResponse.append(Fragment);
                        sink.next(Fragment);
                    })
                    .doOnComplete(() -> {
                        String completeRes = fullResponse.toString();
                        //将ai返回的内容保存到数据库
                      consultationMessageService.saveAiMassage(dbSeeionId,completeRes,"openai");
                      //添加ai回复到chatMemory
                       List<Message> aiMessage = new ArrayList<>();
                       aiMessage.add(new AssistantMessage(completeRes));
                       chatMemory.add(conversationId,aiMessage);

                       sink.complete();
                    })
                    .doOnError(error ->{
                        sink.error(error);
                            })
                    .subscribe();//订阅并启动
        });
    }
    //获取参数中的sessionID
    public Long extractSessional(String sessionId){
        if(sessionId != null && sessionId.startsWith("session_")){
           /* return Long.parseLong(sessionId.substring("session_".length()));*/
            String idStr = sessionId.substring("session_".length());
            return Long.parseLong(idStr);
        }
        return null;
    }
}
