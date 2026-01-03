package com.tantai.uristudy.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@Builder
public class UserCreationRequest {
    @NotBlank(message = "Họ tên không được để trống")
    @Pattern(
            regexp = "^(\\p{Lu}\\p{Ll}+)(\\s\\p{Lu}\\p{Ll}+)*$",
            message = "Mỗi từ phải bắt đầu hoa, chỉ chứa chữ (Unicode), không số/ký tự đặc biệt, không khoảng trắng thừa"
    )
    String name;

    @NotBlank(message = "Email không được để trống")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "Email không hợp lệ"
    )
    String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(
            regexp = "^[0-9]+$",
            message = "Số điện thoại không hợp lệ"
    )
    String phoneNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Ngày sinh không được để trống")
    @PastOrPresent(message = "Ngày sinh không được lớn hơn ngày hiện tại")
    LocalDate dateOfBirth;

    @NotNull(message = "Giới tính không được để trống")
    Boolean isMale;

    @NotBlank(message = "Địa chỉ không được để trống")
    String address;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 100, message = "Tên đăng nhập phải từ 3 đến 100 ký tự")
    @Pattern(
            regexp = "^[a-zA-Z0-9]([._](?![._])|[a-zA-Z0-9]){1,18}[a-zA-Z0-9]$",
            message = "Chỉ cho phép chữ thường, chữ hoa, số, dấu gạch dưới (_) và dấu chấm (.)"
    )
    String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 3, max = 100, message = "Mật khẩu phải từ 3 đến 100 ký tự")
    String password;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 3, max = 100, message = "Xác nhận mật khẩu phải từ 3 đến 100 ký tự")
    String confirmPassword;
}
