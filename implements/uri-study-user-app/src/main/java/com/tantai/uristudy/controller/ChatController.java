package com.tantai.uristudy.controller;

import com.tantai.uristudy.dto.request.ChatRequest;
import com.tantai.uristudy.security.CustomUserDetails;
import com.tantai.uristudy.service.ChatService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.chat.messages.Message;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatController {
    ChatService chatService;

    @GetMapping("/chat")
    public String showChatBox(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        List<Message> messages = chatService.getMemoryByConversationId(customUserDetails.getUser().getId());
        model.addAttribute("messages", messages);

        return "chat-box";
    }

    @PostMapping("/do-chat")
    public String doChat(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @ModelAttribute("chatRequest") ChatRequest chatRequest,
            Model model
    ) {
        if (chatRequest.getMessage().isBlank() && chatRequest.getImage().isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập nội dung bạn muốn hỏi 😊");
            return "chat-box";
        }

        String response = chatService.chat(customUserDetails.getUser().getId(), chatRequest);
        model.addAttribute("response", response);

        List<Message> messages = chatService.getMemoryByConversationId(customUserDetails.getUser().getId());
        model.addAttribute("messages", messages);

        return "chat-box";
    }
}
