package com.example.oauthloginproject.service;

import com.example.oauthloginproject.config.auth.JwtAuthProvider;
import com.example.oauthloginproject.domain.Member;
import com.example.oauthloginproject.dto.AuthenticationDto;
import com.example.oauthloginproject.dto.LoginDto;
import com.example.oauthloginproject.exception.custom.ForbiddenException;
import com.example.oauthloginproject.exception.custom.UserNotFoundException;
import com.example.oauthloginproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    private final JwtAuthProvider authProvider;

    // 회원 조회
    public String loginMember(LoginDto loginDto) { // controller 에서 인증거친 사용자 데이터
//AuthenticationDto
//        //dto -> entity
        Member loginEntity = loginDto.toEntity();
        Member member = memberRepository.findByEmail(loginEntity.getEmail()).orElse(null);
        // 회원 엔티티 객체 생성 및 조회시작

        if(member == null) {
            throw new UserNotFoundException("메일을 다시 확인해주세요.");
        }

        if(!passwordEncoder.matches(loginEntity.getPassword(), member.getPassword()))
            throw new ForbiddenException("비밀번호를 다시 확인해주세요.");
        AuthenticationDto authentication = modelMapper.map(member, AuthenticationDto.class);

        String accessToken = authProvider.createAccessToken(authentication.getId(), authentication.getEmail(), "USER");

    // Member member = memberRepository.findByEmail(loginEntity.getEmail()).orElseThrow(() -> new UserNotFoundException("User Not Found!!"));

        // 회원 정보를 인증 클래스 객체로 매핑
//        return modelMapper.map(member, AuthenticationDto.class); // member의 값을 authenticationdto 로 매핑하고 dto에 저장
        // modelmapper란 어떤 object에 있는 필드값을 자동으로 원하는 object로 mapping시켜준다.

        return accessToken;
        
    }
}
