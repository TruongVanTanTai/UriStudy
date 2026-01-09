package com.tantai.uristudy.controller;

import com.tantai.uristudy.dto.request.FlashCardSetCreationRequest;
import com.tantai.uristudy.dto.request.FlashCardSetEditRequest;
import com.tantai.uristudy.entity.FlashCardSet;
import com.tantai.uristudy.security.CustomUserDetails;
import com.tantai.uristudy.service.FlashCardSetService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FlashCardSetController {
    FlashCardSetService flashCardSetService;

    @GetMapping("/flash-card-sets")
    public String showFlashCardSets(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "searchKey", required = false) String searchKey,
            @RequestParam(name = "mode", defaultValue = "all") String mode,
            @RequestParam(name = "page", defaultValue = "1") int page,
            Model model
    ) {
        Long userId = customUserDetails.getUser().getId();
        Page<FlashCardSet> flashCardSets;
        if (searchKey != null) {
            flashCardSets = flashCardSetService.getFlashCardSetsByIdAndByName(userId, searchKey, page, 6);
            model.addAttribute("searchKey", searchKey);
        }
        else if (mode.equals("all")) {
            flashCardSets = flashCardSetService.getFlashCardSetsByUserId(userId, page, 6);
        }
        else if (mode.equals("vocabulary")) {
            flashCardSets = flashCardSetService.getFlashCardSetsByUserIdAndType(userId, false, page, 6);
        }
        else if (mode.equals("grammar")) {
            flashCardSets = flashCardSetService.getFlashCardSetsByUserIdAndType(userId, true, page, 6);
        }
        else {
            flashCardSets = flashCardSetService.getFavoriteFlashCardSets(userId, page, 6);
        }

        model.addAttribute("mode", mode);
        model.addAttribute("flashCardSets", flashCardSets.getContent());
        model.addAttribute("page", flashCardSets.getNumber() + 1);
        model.addAttribute("numberOfPages", flashCardSets.getTotalPages());

        return "flash-card-sets";
    }

    @GetMapping("flash-card-set/create")
    public String showFlashCardSetCreationForm(Model model) {
        model.addAttribute("flashCardSetCreationRequest", new FlashCardSetCreationRequest());
        return "flash-card-set-creation-form";
    }


    @PostMapping("/flash-card-set/do-create")
    public String createFlashCardSet(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @ModelAttribute("flashCardSetCreationRequest") FlashCardSetCreationRequest flashCardSetCreationRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "flash-card-set-creation-form";
        }

        flashCardSetService.createFlashCardSet(customUserDetails.getUser().getId(), flashCardSetCreationRequest);
        redirectAttributes.addFlashAttribute("message", "Thêm mới bộ flash card thành công");
        return "redirect:/flash-card-sets";
    }

    @GetMapping("/flash-card-set/edit/{id}")
    public String showFlashCardSetEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("flashCardSetEditRequest", flashCardSetService.getFlashCardSetById(id));
        return "flash-card-set-edit-form";
    }

    @PostMapping("/flash-card-set/do-edit")
    public String editFlashCardSet(
            @Valid @ModelAttribute("flashCardSetEditRequest") FlashCardSetEditRequest flashCardSetEditRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "flash-card-set-edit-form";
        }

        flashCardSetService.editFashCartSet(flashCardSetEditRequest);
        redirectAttributes.addFlashAttribute("message", "Cập nhật bộ flash card thành công");
        return "redirect:/flash-card-sets";
    }

    @GetMapping("/flash-card-set/delete/{id}")
    public String deleteFlashCardSet(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        FlashCardSet flashCardSet = flashCardSetService.getFlashCardSetById(id);
        flashCardSetService.deleteFlashCardSet(flashCardSet);
        redirectAttributes.addFlashAttribute("message", "Xóa bộ flash card thành công");
        return "redirect:/flash-card-sets";
    }

    @GetMapping("/flash-card-set/update-share-status/{id}")
    public String updateFlashCardSetShareStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") boolean status,
            RedirectAttributes redirectAttributes
    ) {
        flashCardSetService.updateShareStatus(id, status);
        redirectAttributes.addFlashAttribute("message", "Đã cập nhật trạng thái chia sẻ bộ flash card thành công");
        return "redirect:/flash-card-sets";
    }

    @GetMapping("/flash-card-set/play/{id}")
    public String getFlashCardsToPlay(@PathVariable("id") Long id, Model model) {
        model.addAttribute("flashCardSet", flashCardSetService.getFlashCardSetToPlay(id));
        return "flash-card-set-play";
    }
}
