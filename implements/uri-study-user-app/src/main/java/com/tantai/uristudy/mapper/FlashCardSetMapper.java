package com.tantai.uristudy.mapper;

import com.tantai.uristudy.dto.request.FlashCardSetCreationRequest;
import com.tantai.uristudy.entity.FlashCardSet;
import org.springframework.stereotype.Component;

@Component
public class FlashCardSetMapper {
    public FlashCardSet toFlashCardSet(FlashCardSetCreationRequest flashCardSetCreationRequest) {
        FlashCardSet flashCardSet = FlashCardSet.builder()
                .name(flashCardSetCreationRequest.getName())
                .type(flashCardSetCreationRequest.getType())
                .description(flashCardSetCreationRequest.getDescription())
                .isPublic(flashCardSetCreationRequest.getIsPublic())
                .build();

        return flashCardSet;
    }
}
