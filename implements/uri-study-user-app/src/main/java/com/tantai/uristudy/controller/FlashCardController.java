package com.tantai.uristudy.controller;

import com.tantai.uristudy.dto.request.FlashCardCreationRequest;
import com.tantai.uristudy.dto.request.FlashCardEditRequest;
import com.tantai.uristudy.entity.FlashCard;
import com.tantai.uristudy.repository.FlashCardRepository;
import com.tantai.uristudy.security.CustomUserDetails;
import com.tantai.uristudy.service.FlashCardService;
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
public class FlashCardController {
    FlashCardService flashCardService;
    private final FlashCardRepository flashCardRepository;
    private final FlashCardSetService flashCardSetService;

    @GetMapping("/flash-card-set/{flashCardSetId}/flash-cards")
    public String getFlashCardByFlashCardSetId(
            @PathVariable("flashCardSetId") Long flashCardSetId,
            @RequestParam(name = "searchKey", required = false) String searchKey,
            @RequestParam(name = "page", defaultValue = "1") int page,
            Model model
    ) {
        model.addAttribute("flashCardSet", flashCardSetService.getFlashCardSetById(flashCardSetId));
        Page<FlashCard> flashCards;
        if (searchKey != null) {
            flashCards = flashCardService.searchFlashCard(flashCardSetId, searchKey, page, 10);
            model.addAttribute("searchKey", searchKey);
        }
        else {
            flashCards = flashCardService.getFlashCardByFlashCardSetId(flashCardSetId, page, 10);
        }

        model.addAttribute("flashCards", flashCards.getContent());
        model.addAttribute("page", flashCards.getNumber() + 1);
        model.addAttribute("totalPages", flashCards.getTotalPages());
        return "flash-cards";
    }

    @GetMapping("/flash-card-set/{flashCardSetId}/flash-card/create")
    public String showFlashCardCreationForm(@PathVariable("flashCardSetId") Long flashCardSetId, Model model) {
        model.addAttribute("flashCardCreationRequest", new FlashCardCreationRequest());
        return "flash-card-creation-form";
    }

    @PostMapping("/flash-card-set/{flashCardSetId}/flash-card/do-create")
    public String createFlashCard(
            @PathVariable("flashCardSetId") Long flashCardSetId,
            @Valid @ModelAttribute FlashCardCreationRequest flashCardCreationRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("flashCartSetid", flashCardSetId);
            return "flash-card-creation-form";
        }

        flashCardService.createFlashCard(flashCardCreationRequest);
        redirectAttributes.addFlashAttribute("message", "Thêm mới flash card thành công");
        return "redirect:/flash-card-set/" + flashCardSetId + "/flash-cards";
    }

    @GetMapping("/flash-card-set/{flashCardSetId}/flash-card/edit/{flashCardId}")
    public String showFlashCardEditForm(
            @PathVariable("flashCardSetId") Long flashCardSetId,
            @PathVariable("flashCardId") Long flashCardId,
            Model model
    ) {
        model.addAttribute("flashCardSetId", flashCardSetId);
        model.addAttribute("flashCardId", flashCardId);
        model.addAttribute("flashCardEditRequest", flashCardService.getFlashCardById(flashCardId));
        return "flash-card-edit-form";
    }

    @PostMapping("/flash-card-set/{flashCardSetId}/flash-card/do-edit/{flashCardId}")
    public String editFlashCard(
            @PathVariable("flashCardSetId") Long flashCardSetId,
            @PathVariable("flashCardId") Long flashCardId,
            @Valid @ModelAttribute("flashCardEditRequest") FlashCardEditRequest flashCardEditRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("flashCardSetId", flashCardSetId);
            model.addAttribute("flashCardId", flashCardId);
            return "flash-card-edit-form";
        }

        flashCardService.updateFlashCard(flashCardEditRequest);
        redirectAttributes.addFlashAttribute("message", "Cập nhật flash card thành công");
        return "redirect:/flash-card-set/" + flashCardSetId + "/flash-cards";
    }

    @GetMapping("/flash-card-set/{flashCardSetId}/flash-card/delete/{flashCardId}")
    public String deleteFlashCard(
            @PathVariable("flashCardSetId") Long flashCardSetId,
            @PathVariable("flashCardId") Long flashCardId,
            RedirectAttributes redirectAttributes
    ) {
        FlashCard flashCard = flashCardService.getFlashCardById(flashCardId);
        flashCardService.deleteFlashCardById(flashCard);
        redirectAttributes.addFlashAttribute("message", "Đã xóa flash card thành cồng");
        return "redirect:/flash-card-set/" + flashCardSetId + "/flash-cards";
    }

    @GetMapping("/flash-card-set/{flashCardSetId}/flash-card/add-to-favorite/{flashCardId}")
    public String addToFavoriteFlashCard(
            @PathVariable("flashCardSetId") Long flashCardSetId,
            @PathVariable("flashCardId") Long flashCardId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            RedirectAttributes redirectAttributes
    ) {
        flashCardService.addToFavoriteFlashCardSet(customUserDetails.getUser().getId(), flashCardId);
        redirectAttributes.addFlashAttribute("message", "Đã thêm vào bộ flash cart yêu thích thành công");
        return "redirect:/flash-card-set/" + flashCardSetId + "/flash-cards";
    }
}
