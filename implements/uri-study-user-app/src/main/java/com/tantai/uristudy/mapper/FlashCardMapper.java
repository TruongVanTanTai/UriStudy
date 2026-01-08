package com.tantai.uristudy.mapper;

import com.tantai.uristudy.dto.request.FlashCardCreationRequest;
import com.tantai.uristudy.entity.FlashCard;
import org.springframework.stereotype.Component;

@Component
public class FlashCardMapper {
    public FlashCard toFlashCard(FlashCardCreationRequest flashCardCreationRequest) {
        FlashCard flashCard = FlashCard.builder()
                .term(flashCardCreationRequest.getTerm())
                .definition(flashCardCreationRequest.getDefinition())
                .note(flashCardCreationRequest.getNote())
                .example(flashCardCreationRequest.getExample())
                .build();

        return flashCard;
    }
}
