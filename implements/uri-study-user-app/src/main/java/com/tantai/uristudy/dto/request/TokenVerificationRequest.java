package com.tantai.uristudy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Setter
@Getter
public class TokenVerificationRequest {
    @NotNull(message = "Token id là bắt buộc")
    Long id;

    @NotBlank(message = "Mã token là bắt buộc")
    String token;
}
