package spring.ai.example.spring_ai_demo.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;


@Service
public class ChatService {
    private final ChatModel chatModel;
    public ChatService(ChatModel chatModel){
        this.chatModel=chatModel;
    }

    public String generateResponse(String message){
        ChatResponse chatResponse = chatModel.call(new Prompt(userInput));
        return chatResponse.getResult().getOutput().getText();
    }
    
}
