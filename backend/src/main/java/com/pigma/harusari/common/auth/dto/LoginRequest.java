package com.pigma.harusari.common.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginRequest {

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

}
