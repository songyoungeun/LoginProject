package com.example.oauthloginproject;

import com.example.oauthloginproject.config.auth.JwtAuthProvider;
import com.example.oauthloginproject.domain.Member;
import com.example.oauthloginproject.dto.AuthenticationDto;
import com.example.oauthloginproject.dto.LoginDto;
import com.example.oauthloginproject.repository.UserRepository;
import com.example.oauthloginproject.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@SpringBootTest
@Commit
@RequiredArgsConstructor
public class UserTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    LoginService loginService;
    @Autowired
    JwtAuthProvider authProvider;
    @Test
    public void testInsert() {

        Member member = new Member();
        member.setEmail("song@joins.com");
        member.setPassword(passwordEncoder.encode("5481"));
        member.setName("송영은");

        userRepository.save(member);

    }

//    @Test
//    @PostMapping("/api/signin")
//    public ResponseEntity<AuthenticationDto> 로그인테스트 (@Valid @RequestBody LoginDto loginDto) throws Exception { // email, password 넘겨주면됨
//
//        //로그인 요청시
//        //id, pw 있는 인증 후 토큰 만들어서 보내줌 (access, refresh) -> 이거 두개 객체로 묶어서
//        AuthenticationDto authentication = loginService.loginMember(loginDto);
//
//        return ResponseEntity.ok()
//                .header("accesstoken", authProvider
//                        .createAccessToken(authentication.getId(),
//                                authentication.getEmail(), "USER"))
//                .body(authentication);
//    }
}
