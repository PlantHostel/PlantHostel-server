package com.planthostelserver.member.dto;

import com.planthostelserver.member.domain.Member;
import com.planthostelserver.type.Gender;
import com.planthostelserver.type.ResidenceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원가입 DTO")
public class MemberDTO {

    @Schema(description = "회원 아이디", type = "String", example = "abc001")
    private String userId;

    @Schema(description = "회원 비밀번호", type = "String", example = "password001")
    private String password;

    @Schema(description = "비밀번호 확인", type = "String", example = "password001")
    private String passwordCheck;

    @Schema(description = "이름", type = "String", example = "홍길동")
    private String name;

    @Schema(description = "닉네임", type = "String", example = "꼬지누")
    private String nickname;

    @Schema(description = "핸드폰 번호", type = "String", example = "01012341234")
    private String hp;

    @Schema(description = "이메일", type = "String", example = "abc001@naver.com")
    private String email;

    @Schema(description = "우편번호", type = "String", example = "16923")
    private String zipcode;

    @Schema(description = "주소", type = "String", example = "경기도 용인시 수지구 진산로 90")
    private String address;

    @Schema(description = "상세 주소", type = "String", example = "502동 901호")
    private String addressDetail;

    @Schema(description = "거주 형태")
    private ResidenceType residenceType;

    @Schema(description = "성별")
    private Gender gender;

    @Schema(description = "선호 식물 종", type = "String", example = "코스모스")
    private String favoritePlant;

    @Schema(description = "자기소개", type = "String", example = "안녕하세요~")
    private String intro;

    public static MemberDTO toDTO(Member member) {
        return MemberDTO.builder()
            .userId(member.getUserId())
            .password(member.getPassword())
            .name(member.getName())
            .nickname(member.getNickname())
            .hp(member.getHp())
            .email(member.getEmail())
            .zipcode(member.getZipcode())
            .address(member.getAddress())
            .addressDetail(member.getAddressDetail())
            .residenceType(member.getResidenceType())
            .gender(member.getGender())
            .favoritePlant(member.getFavoritePlant())
            .intro(member.getIntro())
            .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberJoinDTO {

        private String userId;
        private String name;
        private String nickname;
        private String hp;
        private String email;
        private String intro;
    }
}
