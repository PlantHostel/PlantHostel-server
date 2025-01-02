package com.planthostelserver.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.planthostelserver.auth.user.PrincipalDetails;
import com.planthostelserver.member.domain.Member;
import com.planthostelserver.type.JwtTokenType;
import com.planthostelserver.type.RoleType;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String KEY_ID = "id";
    private static final String KEY_ROLE = "role";
    private static final String TOKEN_TYPE = "type";

    private static String jwtSecretKey;

    @Value("${spring.jwt.secret}")
    public void setKey(String key) {
        jwtSecretKey = key;
    }

    public static String createToken(PrincipalDetails principalDetails, JwtTokenType jwtTokenType) {
        log.info("JWT {} 발급 user = {}", jwtTokenType.name(), principalDetails.getUsername());

        Date now = new Date(System.currentTimeMillis());
        Date expireDate = new Date(now.getTime() + jwtTokenType.getExpireTime());

        return makeToKenDetails(principalDetails, now, expireDate, jwtTokenType.name());
    }

    public static PrincipalDetails verify(String token) throws JWTVerificationException {
        log.info("토큰 검증 {}", token);

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(jwtSecretKey)).build().verify(token);

        Long id = decodedJWT.getClaim(KEY_ID).asLong();
        String userId = decodedJWT.getSubject();
        String role = decodedJWT.getClaim(KEY_ROLE).asString();

        Member member = Member.builder()
            .memberId(id)
            .userId(userId)
            .role(RoleType.valueOf(role))
            .build();

        return new PrincipalDetails(member);
    }

    public static boolean isExpiredToken(String token) {
        return JWT.decode(token).getExpiresAt().before(new Date(System.currentTimeMillis()));
    }

    private static String makeToKenDetails(PrincipalDetails principalDetails, Date now,
        Date expireDate, String tokenType) {

        return JWT.create().withSubject(principalDetails.getUsername())
            .withIssuedAt(now)
            .withExpiresAt(expireDate)
            .withClaim(KEY_ID, principalDetails.getMember().getMemberId())
            .withClaim(KEY_ROLE, principalDetails.getMember().getRole().name())
            .withClaim(TOKEN_TYPE, tokenType)
            .sign(Algorithm.HMAC512(jwtSecretKey));
    }
}
