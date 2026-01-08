package com.tantai.uristudy.controller;

import com.tantai.uristudy.dto.request.UserCreationRequest;
import com.tantai.uristudy.dto.request.UserEditRequest;
import com.tantai.uristudy.exception.PasswordMismatchException;
import com.tantai.uristudy.exception.UserCreationException;
import com.tantai.uristudy.exception.UsernameAlreadyExistsException;
import com.tantai.uristudy.security.CustomUserDetails;
import com.tantai.uristudy.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    UserService userService;

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

    @GetMapping("/profile")
    public String showProfileForm(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Model model
    ) {
        model.addAttribute("user", userService.getUserById(customUserDetails.getUser().getId()));
        return "user-profile-form";
    }

    @GetMapping("/edit")
    public String showEditForm(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Model model
    ) {
        model.addAttribute("userEditRequest", userService.getUserById(customUserDetails.getUser().getId()));
        return "user-edit-form";
    }

    @PostMapping("/do-edit")
    public String editUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @ModelAttribute("userEditRequest") UserEditRequest userEditRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "user-edit-form";
        }

        userService.updateUser(customUserDetails.getUser().getId(), userEditRequest);
        redirectAttributes.addFlashAttribute("message", "Đã cập nhật thông tin cá nhân thành công");
        return "redirect:/user/profile";
    }
}
