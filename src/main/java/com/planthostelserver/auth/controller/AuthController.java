package com.planthostelserver.auth.controller;

import com.planthostelserver.auth.service.AuthService;
import com.planthostelserver.common.dto.ResponseDTO;
import com.planthostelserver.member.dto.MemberDTO;
import com.planthostelserver.member.dto.MemberDTO.MemberJoinDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
@Tag(name = "Auth", description = "회원가입 API")
public class AuthController {

    private final AuthService authService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = MemberDTO.MemberJoinDTO.class)))
    })
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody MemberDTO memberDTO) {
        MemberJoinDTO result = authService.join(memberDTO);

        return ResponseEntity.ok(
            new ResponseDTO<>(HttpStatus.CREATED.value(), "회원가입이 완료되었습니다.", result)
        );
    }
}
