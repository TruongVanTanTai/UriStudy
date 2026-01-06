package com.tantai.uristudy.controller;

import com.tantai.uristudy.dto.request.EmailVerificationRequest;
import com.tantai.uristudy.dto.request.TokenVerificationRequest;
import com.tantai.uristudy.exception.EmailAlreadyExistsException;
import com.tantai.uristudy.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    AuthService authService;

    @GetMapping("/verifyEmail")
    public String showEmailVerificationForm(Model model) {
        model.addAttribute("emailVerificationRequest", new EmailVerificationRequest());
        return "email-verification-form";
    }

    @PostMapping("/sendEmailVerification")
    public String sendEmailVerification(
            @Valid @ModelAttribute("emailVerificationRequest") EmailVerificationRequest emailVerificationRequest,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "email-verification-form";
        }

        try {
            authService.sendEmailVerification(emailVerificationRequest);
        }
        catch (EmailAlreadyExistsException e) {
            model.addAttribute("message", e.getMessage());
            return "email-verification-form";
        }
        catch (RuntimeException e) {
            model.addAttribute("message", "Gửi email không thành công");
            return "email-verification-form";
        }

        model.addAttribute("message", "Thư đã được gửi đến email của bạn. Vui lòng đăng nhập email để xác thực!");
        return "email-verification-form";
    }

    @GetMapping("/verifyToken")
    public String verifyToken(
            @Valid @ModelAttribute("tokenVerificationRequest")TokenVerificationRequest tokenVerificationRequest,
            HttpSession httpSession
    ) {
        String email = authService.verifyToken(tokenVerificationRequest);
        httpSession.setAttribute("email", email);
        return "redirect:/user/register";
    }
}
