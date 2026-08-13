package org.example.ai_mha.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ChatClientConfig {
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
        .maxMessages(30)
        .build();
    }

@Bean("open-ai")
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel) {
    return ChatClient.builder(openAiChatModel)
            .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory()).build())
            .defaultSystem("你是一个专业的心理健康疏导师，和蔼可亲，温柔善良，能够提供专业的心理健康咨询和建议")
            .build();
    }
}
