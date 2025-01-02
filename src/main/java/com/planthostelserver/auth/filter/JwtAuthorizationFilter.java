package com.planthostelserver.auth.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planthostelserver.auth.jwt.JwtTokenProvider;
import com.planthostelserver.auth.user.PrincipalDetails;
import com.planthostelserver.common.dto.ResponseDTO;
import com.planthostelserver.error.type.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    // Swagger 접근할 때 필터를 타지 않도록 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();

        return path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        log.info("{} JWT 토큰 유효성 검사", request.getRequestURL());

        String header = request.getHeader(TOKEN_HEADER);

        if (!(header != null && header.startsWith(TOKEN_PREFIX))) {
            log.info("JwtAuthorizationFilter : 토큰 에러");
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken =
            !ObjectUtils.isEmpty(header) && header.startsWith(TOKEN_PREFIX) ? header.substring(
                TOKEN_PREFIX.length()) : null;

        if (isRefreshToken(accessToken)) {
            log.error("Refresh Token 사용");

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ResponseDTO<?> responseDTO = ResponseDTO.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ErrorResponse.DO_NOT_USE_REFRESH_TOKEN.getMessage())
                .build();

            new ObjectMapper().writeValue(response.getOutputStream(), responseDTO);

            return;
        }

        if (JwtTokenProvider.isExpiredToken(accessToken)) {
            log.info("만료된 토큰입니다.");

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ResponseDTO<?> responseDTO = ResponseDTO.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ErrorResponse.EXPIRED_TOKEN.getMessage())
                .build();

            new ObjectMapper().writeValue(response.getOutputStream(), responseDTO);

            return;
        }

        PrincipalDetails principalDetails = JwtTokenProvider.verify(accessToken);

        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,
            null, principalDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private boolean isRefreshToken(String token) {
        try {
            String type = JWT.decode(token).getClaim("type").asString();

            return "REFRESH_TOKEN".equals(type);
        } catch (JWTDecodeException e) {
            log.error("JWT decode 에러");
            return false;
        }
    }
}
