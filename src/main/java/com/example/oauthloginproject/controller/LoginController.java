package com.example.oauthloginproject.controller;

import com.example.oauthloginproject.config.auth.JwtAuthProvider;
import com.example.oauthloginproject.domain.Member;
import com.example.oauthloginproject.dto.AuthenticationDto;
import com.example.oauthloginproject.dto.LoginDto;
import com.example.oauthloginproject.exception.custom.ForbiddenException;
import com.example.oauthloginproject.exception.custom.UserNotFoundException;
import com.example.oauthloginproject.repository.UserRepository;
import com.example.oauthloginproject.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final UserRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
//    private final JwtAuthProvider authProvider;
    private final ModelMapper modelMapper;


    @PostMapping("/signin")
    public ResponseEntity<String> login(HttpServletResponse response, @Valid @RequestBody LoginDto loginDto) throws Exception { //로그인 api

        // 로그인 요청시 -> LoginDto 에 담겨 보내짐
        // id, pw 있는 인증 후 토큰 만들어서 보내줌 (access)
        // 토큰 만드는 부분은 서비스에 구현하는 것인가? - > 서비스 로직으로 빼기
        // 쿠키에 토큰 실어서 넣어주기

        // controller : 받은 데이터에 대한 예외처리 , 예) 잘못된 사용자 , 아이디 비밀번호 유효성 검사 , 서비스 내부로 들어가기 전 예외 사항 쳐내 
        // 서비스에서는 이미 인증된 사용자라는 걸 기준잡고 로직을 짬
        // 404 요청 에러 -> http 헤더에 정의,
        // 500번대 서버에러
        // service : 예) 고객으 정보 가져오거나
        //dto -> entity

        String accessToken = loginService.loginMember(loginDto);
        //쿠키에 token 실어서 보내기
        Cookie cookie = new Cookie("Authorization", accessToken);
        cookie.setMaxAge(60 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().body("login success");

    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response, HttpServletRequest request) { // 로그아웃 api

        //원래 쿠키의 이름이 userInfo 이었다면, value를 null로 처리.
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0); // 쿠키의 expiration 타임을 0으로 하여 없앤다.
        cookie.setPath("/"); // 모든 경로에서 삭제 됬음을 알린다.
        response.addCookie(cookie);

        return ResponseEntity.ok().body("logout success");
    }

}
