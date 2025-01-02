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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
public class JwtAuthorizationFailureHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.error("접근 권한 없음");

        ResponseDTO<Object> responseDTO = ResponseDTO.builder()
            .status(HttpStatus.FORBIDDEN.value())
            .message(ErrorResponse.FORBIDDEN.getMessage())
            .build();

        new ObjectMapper().writeValue(response.getOutputStream(), responseDTO);
    }
}
