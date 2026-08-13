package org.example.ai_mha.controller;

import cn.hutool.json.JSONUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.Valid;
import org.example.ai_mha.AiService.PsychologicalSupportService;
import org.example.ai_mha.AiService.StructOutPut;
import org.example.ai_mha.DTO.command.ConsultationSessionCreateDTO;
import org.example.ai_mha.DTO.command.ConsultationStreamDTO;
import org.example.ai_mha.common.Result;
import org.example.ai_mha.common.ResultCode;
import org.example.ai_mha.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/psychological-chat")
public class PsychologicalChat {
    @Autowired
    private PsychologicalSupportService psychologicalSupportService;
    @PostMapping("/session/start")
    public Result<StructOutPut.StreamChatSession>startSession(@Valid @RequestBody ConsultationSessionCreateDTO createDTO){
        //获取当前用户
        String token = JwtTokenUtil.getCurrentToken();
        DecodedJWT jwt = JwtTokenUtil.verifyToken(token);
        Long userId = jwt.getClaim("userId").asLong();
        //调用service
        StructOutPut.StreamChatSession session = psychologicalSupportService.startSession(userId,createDTO);
        return Result.success(session);
    }

    @PostMapping(value = "stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChat(@Valid @RequestBody ConsultationStreamDTO createDTO){
        String token = JwtTokenUtil.getCurrentToken();
        DecodedJWT jwt = JwtTokenUtil.verifyToken(token);
        Long userId = jwt.getClaim("userId").asLong();

        if(userId == null){
            return Flux.just(ServerSentEvent.<String>builder()
                    .event("error")
                    .data(JSONUtil.toJsonStr(Result.error(ResultCode.UNAUTHORIZED.getCode(),ResultCode.UNAUTHORIZED.getMsg(),"用户未登录")))
                    .build());
        }

        //开始留式对话
        return null;
    }
}
