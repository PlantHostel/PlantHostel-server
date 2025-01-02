package com.planthostelserver.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {

    @Getter
    @Setter
    @Schema(description = "로그인 DTO")
    public static class LoginRequestDTO {

        @Schema(description = "회원 아이디", type = "String", example = "abc001")
        private String userId;
        @Schema(description = "회원 비밀번호", type = "String", example = "password001")
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginResponseDTO {

        private String accessToken;
    }


}
