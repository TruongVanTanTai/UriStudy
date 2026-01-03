package com.tantai.uristudy.controller;

import com.tantai.uristudy.dto.request.UserCreationRequest;
import com.tantai.uristudy.exception.PasswordMismatchException;
import com.tantai.uristudy.exception.UserCreationException;
import com.tantai.uristudy.exception.UsernameAlreadyExistsException;
import com.tantai.uristudy.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model, HttpSession httpSession) {
        if (httpSession.getAttribute("email") == null) {
            throw new UserCreationException("Đăng ký tài khoản thất bại");
        }
        model.addAttribute("userCreationRequest", new UserCreationRequest());
        return "register-form";
    }

    @PostMapping("/create")
    public String createUser(
            @Valid @ModelAttribute("userCreationRequest") UserCreationRequest userCreationRequest,
            BindingResult bindingResult,
            HttpSession httpSession,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (httpSession.getAttribute("email") == null || !httpSession.getAttribute("email").equals(userCreationRequest.getEmail())) {
            throw new UserCreationException("Đăng ký tài khoản thất bại");
        }
        if (bindingResult.hasErrors()) {
            return "register-form";
        }

        try {
            userService.createUser(userCreationRequest);
        }
        catch (PasswordMismatchException e) {
            model.addAttribute("message", e.getMessage());
            return "register-form";
        }
        catch (UsernameAlreadyExistsException e) {
            model.addAttribute("message", e.getMessage());
            return "register-form";
        }
        redirectAttributes.addFlashAttribute("message", "Đăng ký tài khoản thành công");
        return "redirect:/login";
    }
}
