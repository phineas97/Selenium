package spring.ai.example.spring_ai_demo.controller;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.ai.example.spring_ai_demo.config.ChatClientConfig;
import spring.ai.example.spring_ai_demo.service.ChatService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService){
        this.chatService=chatService;
    }

    @PostMapping("/simple")
    public ResponseEntity<ChatResponse> simplechat(@RequestBody @Valid ChatRequest request){
        try{
            String response = chatService.generateResponse(request.message());
            return ResponseEntity.ok(new ChatResponse(true,response,null));
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(new ChatResponse(false,null,"对话生成失败"+e.getMessage()));
        }
    }

    @PostMapping(value="/stream",produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<ChatResponse> streamchat(@RequestBody @Valid ChatRequest request){
        try{
            String response = chatService.generateResponse(request.message());
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body("流式对话生成失败"+e.getMessage()));
        }
    }


    public record ChatResponse(
            boolean success,
            String Data,
            String error
    ){}
}
