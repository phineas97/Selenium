package com.example.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolConfig {
   @Autowired
    private SyncMcpToolCallbackProvider tools;

   @Autowired
    private ChatClient.Builder builder;

    @Bean
    public ChatClient chatClient() {
        return builder.defaultToolCallbacks(tools)
                .build();
    }
}
