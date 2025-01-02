package com.planthostelserver.member.domain;

import com.planthostelserver.member.dto.MemberDTO;
import com.planthostelserver.type.Gender;
import com.planthostelserver.type.ResidenceType;
import com.planthostelserver.type.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String hp;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String zipcode;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String addressDetail;

    @Column(nullable = false)
    private RoleType role;

    private String refreshToken;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResidenceType residenceType;

    private String favoritePlant;

    private String intro;

    public static Member toEntity(MemberDTO memberDTO) {
        return Member.builder()
            .userId(memberDTO.getUserId())
            .password(memberDTO.getPassword())
            .name(memberDTO.getName())
            .nickname(memberDTO.getNickname())
            .hp(memberDTO.getHp())
            .email(memberDTO.getEmail())
            .zipcode(memberDTO.getZipcode())
            .address(memberDTO.getAddress())
            .addressDetail(memberDTO.getAddressDetail())
            .gender(memberDTO.getGender())
            .residenceType(memberDTO.getResidenceType())
            .favoritePlant(memberDTO.getFavoritePlant())
            .intro(memberDTO.getIntro())
            .role(RoleType.USER)
            .build();
    }
}
