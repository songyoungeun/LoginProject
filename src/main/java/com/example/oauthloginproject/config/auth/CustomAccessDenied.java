package com.example.oauthloginproject.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAccessDenied implements AccessDeniedHandler { //인가예외처리

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        response.sendRedirect("/exception/accessdenied");
        //특정 처리 후, 또는 특정 조건일 때에 지정한 페이지로 이동하고 싶은 경우 많이 사용
    }

}
