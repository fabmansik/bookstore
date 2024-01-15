package com.milansomyk.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private int userId;
    @NotBlank(message = "username required")
    @Size(min = 3, max = 20, message = "username: min: {min}, max: {max} characters")
    private String username;
    @NotBlank(message = "password required")
    @Pattern(regexp = "^(?=.*\\d).{4,8}$", flags = Pattern.Flag.UNICODE_CASE, message = "invalid password")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    @Email(message = "Not a email")
    private String email;
    @NotNull(message = "phone number is required")
    private int phone;

}
