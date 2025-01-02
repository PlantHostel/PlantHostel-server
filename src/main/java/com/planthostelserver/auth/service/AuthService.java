package com.planthostelserver.auth.service;

import com.planthostelserver.auth.user.PrincipalDetails;
import com.planthostelserver.member.domain.Member;
import com.planthostelserver.member.dto.MemberDTO;
import com.planthostelserver.member.dto.MemberDTO.MemberJoinDTO;
import com.planthostelserver.error.exception.AuthException;
import com.planthostelserver.error.type.ErrorResponse;
import com.planthostelserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberDTO.MemberJoinDTO join(MemberDTO memberDTO) {

        String encodedPassword = encodePassword(memberDTO.getPassword());
        memberDTO.setPassword(encodedPassword);

        Member member = Member.toEntity(memberDTO);
        Member savedMember = memberRepository.save(member);

        return MemberJoinDTO.builder()
            .userId(savedMember.getUserId())
            .email(savedMember.getEmail())
            .hp(savedMember.getHp())
            .name(savedMember.getName())
            .nickname(savedMember.getNickname())
            .intro(savedMember.getIntro())
            .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserIdAndDeletedAtIsNull(username)
            .orElseThrow(() -> new AuthException(ErrorResponse.LOGIN_FAIL));

        log.info("사용자 아이디: {}", member.getUserId());

        return new PrincipalDetails(member);
    }

    private String encodePassword(String originPassword) {
        return passwordEncoder.encode(originPassword);
    }

}
