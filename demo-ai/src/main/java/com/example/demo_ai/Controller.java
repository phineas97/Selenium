package com.example.demo_ai;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Map;

@RestController
class Controller {

    // 从配置文件中读取API Key和模型
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.chat.options.model}")
    private String model;

    private final WebClient webClient;

    // 在构造函数中构建WebClient，确保apiKey已经被注入
    public Controller(@Value("${spring.ai.openai.api-key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public static class AiRequest {
        private String userInput;
        public String getUserInput() { return userInput; }
        public void setUserInput(String userInput) { this.userInput = userInput; }
    }

    @PostMapping("/ai")
    String generation(@RequestBody AiRequest request) {

        // 手动构建请求体
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", Collections.singletonList(Map.of("role", "user", "content", request.getUserInput()))
        );

        // 发送POST请求
        JsonNode responseNode = webClient.post()
                .uri("/chat/completions")
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(JsonNode.class) // <-- 这里是关键！将响应体直接映射为 JsonNode
                .block();

        // 从JsonNode中提取content字段
        return responseNode
                .get("choices")
                .get(0)
                .get("message")
                .get("content")
                .asText();

    }
}