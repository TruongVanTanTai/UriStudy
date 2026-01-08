package com.tantai.uristudy.service;

import com.tantai.uristudy.dto.request.FlashCardCreationRequest;
import com.tantai.uristudy.dto.request.FlashCardEditRequest;
import com.tantai.uristudy.entity.FlashCard;
import com.tantai.uristudy.entity.FlashCardSet;
import com.tantai.uristudy.exception.FlashCardNotFoundException;
import com.tantai.uristudy.mapper.FlashCardMapper;
import com.tantai.uristudy.repository.FlashCardRepository;
import com.tantai.uristudy.repository.FlashCardSetRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FlashCardService {
    FlashCardRepository flashCardRepository;
    FlashCardMapper flashCardMapper;
    private final FlashCardSetService flashCardSetService;
    private final FlashCardSetRepository flashCardSetRepository;

    @PreAuthorize("hasAuthority('USER') and @flashCardSetService.getFlashCardSetById(#flashCardSetId) != null")
    public Page<FlashCard> getFlashCardByFlashCardSetId(Long flashCardSetId, int page, int size) {
        if (page <= 0) page = 1;
        if (size <= 0) size = 10;

        Pageable pageable = PageRequest.of(page - 1, size);
        return flashCardRepository.findByFlashCardSetId(flashCardSetId, pageable);
    }

    @PreAuthorize("hasAuthority('USER') and @flashCardSetService.getFlashCardSetById(#flashCardSetId) != null")
    public Page<FlashCard> searchFlashCard(Long flashCardSetId, String searchKey, int page, int size) {
        if (page <= 0) page = 1;
        if (size <= 0) size = 10;

        Pageable pageable = PageRequest.of(page - 1, size);
        return flashCardRepository.searchFlashCard(flashCardSetId, searchKey, pageable);
    }

    @PreAuthorize("hasAuthority('USER') and @flashCardSetService.getFlashCardSetById(#flashCardCreationRequest.flashCardSetId) != null")
    @Transactional
    public FlashCard createFlashCard(FlashCardCreationRequest flashCardCreationRequest) {
        FlashCardSet flashCardSet = flashCardSetService.getFlashCardSetById(flashCardCreationRequest.getFlashCardSetId());
        FlashCard flashCard = flashCardMapper.toFlashCard(flashCardCreationRequest);
        flashCard.setFlashCardSet(flashCardSet);

        flashCardSet.setModifiedDate(LocalDateTime.now());
        flashCardSetRepository.save(flashCardSet);

        return flashCardRepository.save(flashCard);
    }

    @PreAuthorize("hasAuthority('USER') and @flashCardSetService.getFlashCardSetById(#flashCardEditRequest.flashCardSetId) != null")
    @Transactional
    public FlashCard updateFlashCard(FlashCardEditRequest flashCardEditRequest) {
        FlashCard flashCard = flashCardRepository.findByIdAndFlashCardSetId(flashCardEditRequest.getId(), flashCardEditRequest.getFlashCardSetId())
                .orElseThrow(() -> new FlashCardNotFoundException("Không tồn tại flash card có id: " + flashCardEditRequest.getId() + " của bộ flash card có id: " + flashCardEditRequest.getFlashCardSetId()));

        flashCard.setTerm(flashCardEditRequest.getTerm());
        flashCard.setDefinition(flashCardEditRequest.getDefinition());
        flashCard.setNote(flashCardEditRequest.getNote());
        flashCard.setExample(flashCardEditRequest.getExample());

        FlashCardSet flashCardSet = flashCard.getFlashCardSet();
        flashCardSet.setModifiedDate(LocalDateTime.now());
        flashCardSetRepository.save(flashCardSet);

        return flashCardRepository.save(flashCard);
    }

    @PostAuthorize("hasAuthority('USER') and authentication.principal.user.id == returnObject.flashCardSet.user.id")
    public FlashCard getFlashCardById(Long id) {
        return flashCardRepository.findById(id)
                .orElseThrow(() -> new FlashCardNotFoundException("Không tồn tại flash card có id: " + id));
    }

    @PreAuthorize("hasAuthority('USER') and authentication.principal.user.id == #flashCard.flashCardSet.user.id")
    @Transactional
    public void deleteFlashCardById(FlashCard flashCard) {
        flashCardRepository.deleteByIdAndFlashCardSetId(flashCard.getId(), flashCard.getFlashCardSet().getId());
    }

    @PreAuthorize("hasAuthority('USER') and authentication.principal.user.id == #userId")
    @Transactional
    public void addToFavoriteFlashCardSet(Long userId, Long flashCardId) {
        FlashCard flashCard = flashCardRepository.findById(flashCardId)
                .orElseThrow(() -> new FlashCardNotFoundException("Không tồn tại flash card có id: " + flashCardId));

        if (flashCard.getFlashCardSet().getUser().getId() == userId || flashCard.getFlashCardSet().getIsPublic()) {
            FlashCard flashCardToAdd = FlashCard.builder()
                    .term(flashCard.getTerm())
                    .definition(flashCard.getDefinition())
                    .note(flashCard.getNote())
                    .example(flashCard.getExample())
                    .build();

            FlashCardSet flashCardSet;
            if (flashCard.getFlashCardSet().getType()) {
                flashCardSet = flashCardSetRepository.findByUserIdAndIsFavoriteTrueAndTypeTrue(userId);
            }
            else {
                flashCardSet = flashCardSetRepository.findByUserIdAndIsFavoriteTrueAndTypeFalse(userId);
            }
            flashCardSet.setModifiedDate(LocalDateTime.now());
            flashCardSetRepository.save(flashCardSet);
            flashCardToAdd.setFlashCardSet(flashCardSet);
            flashCardRepository.save(flashCardToAdd);
        }
    }
}
