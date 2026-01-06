package com.tantai.uristudy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class FlashCardSetCreationRequest {
    @NotBlank(message = "Tên bộ flash card không được để trống")
    @Size(min = 1, max = 100, message = "Tên bộ flash card phải có từ 1 đên 100 ký tự")
    String name;

    @NotNull(message = "Loại bộ flash card là bắt buộc")
    Boolean type;

    @Size(min = 1, max = 3000, message = "Tên bộ flash card phải có từ 1 đên 3000 ký tự")
    String description;

    @NotNull(message = "Chế độ chia sẻ bộ flash card là bắt buộc")
    Boolean isPublic;

    MultipartFile image;
}
