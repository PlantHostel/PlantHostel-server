package com.planthostelserver.auth.filter;

import static com.planthostelserver.type.JwtTokenType.ACCESS_TOKEN;
import static com.planthostelserver.type.JwtTokenType.REFRESH_TOKEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planthostelserver.auth.dto.AuthDTO;
import com.planthostelserver.auth.dto.AuthDTO.LoginRequestDTO;
import com.planthostelserver.auth.dto.AuthDTO.LoginResponseDTO;
import com.planthostelserver.auth.jwt.JwtTokenProvider;
import com.planthostelserver.auth.user.PrincipalDetails;
import com.planthostelserver.common.dto.ResponseDTO;
import com.planthostelserver.error.exception.AuthException;
import com.planthostelserver.error.type.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String LOGIN_PATH = "/auth/login";

    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
        ObjectMapper objectMapper) {

        setFilterProcessesUrl(LOGIN_PATH);
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        log.info("로그인 시도 : {}", request.getRequestURL());

        try {
            AuthDTO.LoginRequestDTO loginDTO = objectMapper.readValue(request.getInputStream(),
                LoginRequestDTO.class);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginDTO.getUserId(), loginDTO.getPassword());

            return authenticationManager.authenticate(token);

        } catch (IOException e) {
            throw new AuthException(ErrorResponse.LOGIN_FAIL);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authentication)
        throws IOException, ServletException {

        log.info("로그인 성공");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        String accessToken = JwtTokenProvider.createToken(principalDetails, ACCESS_TOKEN);
        String refreshToken = JwtTokenProvider.createToken(principalDetails, REFRESH_TOKEN);

        AuthDTO.LoginResponseDTO loginResponseDTO = new LoginResponseDTO(accessToken);

        ResponseDTO<?> responseDTO = ResponseDTO.builder()
            .status(HttpStatus.OK.value())
            .message("로그인 성공")
            .data(loginResponseDTO)
            .build();

        Cookie refreshTokenCookie = setCookie(REFRESH_TOKEN.name(), refreshToken);

        response.addCookie(refreshTokenCookie);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), responseDTO);
    }

    private static Cookie setCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);

        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }
}
