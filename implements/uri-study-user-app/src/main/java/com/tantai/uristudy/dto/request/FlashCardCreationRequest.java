package com.tantai.uristudy.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@Builder
public class FlashCardCreationRequest {
    @NotBlank(message = "Thuật ngữ là bắt buộc")
    @Size(max = 3000, message = "Thuật ngữ có độ dài tối đa 3000 ký tự")
    String term;

    @NotBlank(message = "Định nghĩa không được để trống")
    @Size(max = 3000, message = "Định nghĩa có độ dài tối đa 3000 ký tự")
    String definition;

    @Size(max = 3000, message = "Ghi chú có độ dài tối đa 3000 ký tự")
    String note;

    @Size(max = 3000, message = "Ví dụ có độ dài tối đa 3000 ký tự")
    String example;

    @NotNull(message = "Mã bộ flash card là bắt buộc")
    @Min(value = 1, message = "Mã bộ flash card phải từ 1 trở lên")
    Long flashCardSetId;
}
