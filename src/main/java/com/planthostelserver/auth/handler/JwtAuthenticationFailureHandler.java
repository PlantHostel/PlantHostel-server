package com.planthostelserver.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planthostelserver.common.dto.ResponseDTO;
import com.planthostelserver.error.type.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
public class JwtAuthenticationFailureHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {

        log.error("로그인 실패");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ResponseDTO<?> responseDTO = ResponseDTO.builder()
            .status(HttpStatus.UNAUTHORIZED.value())
            .message(ErrorResponse.UNAUTHORIZED.getMessage())
            .build();

        new ObjectMapper().writeValue(response.getOutputStream(), responseDTO);

    }
}
