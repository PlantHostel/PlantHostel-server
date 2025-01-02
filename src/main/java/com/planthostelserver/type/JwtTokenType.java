package com.planthostelserver.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtTokenType {
    ACCESS_TOKEN(1000 * 60 * 30), // 30분
    REFRESH_TOKEN(1000 * 60 * 60 * 24 * 7), // 1주일
    ;

    private final long expireTime;
}
