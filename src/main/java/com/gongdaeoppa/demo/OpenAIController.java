package com.gongdaeoppa.demo;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OpenAIController {
    
    private final OpenAIService openAIService;
    
    @PostMapping("/chat")
    //@GetMapping("/chat")
    public ResponseEntity<?> chatWithAI(@RequestParam(value = "prompt", defaultValue = "Tell me a joke") String prompt) {
        
        String response = openAIService.getAIResponse(prompt);
        
        // ✅ 최신 Gson 방식으로 JSON 파싱
        JsonElement element = JsonParser.parseString(response);
        JsonObject jsonObject = element.getAsJsonObject();
        
        // ✅ "choices" 배열 가져오기
        JsonArray choicesArray = jsonObject.getAsJsonArray("choices");
        JsonObject firstChoice = choicesArray.get(0).getAsJsonObject();
        
        // ✅ "message" 객체 가져오기
        JsonObject message = firstChoice.getAsJsonObject("message");
        String content = message.get("content").getAsString();
        
        System.out.println(response);
        
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(new ApiResponse(200, content));
    }
}
