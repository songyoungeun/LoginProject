package com.example.oauthloginproject.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomFilter extends GenericFilterBean {

    //해당 클래스는 JwtTokenProvider가 검증을 끝낸 Jwt로부터 유저 정보를 조회해와서 UserPasswordAuthenticationFilter 로 전달
    private JwtAuthProvider jwtTokenProvider;

    public CustomFilter(JwtAuthProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest)request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        try {
            if("OPTIONS".equalsIgnoreCase(httpReq.getMethod())) { //option 요청일때 필터검증 안함
                httpRes.setStatus(HttpServletResponse.SC_OK);
            } else {
                // 진자 요청일때 필터 검증
                String token = jwtTokenProvider.resolveToken(httpReq); // 헤더에서 JWT 를 받아옵니다.
                if (token != null) { // 유효한 토큰인지 확인합니다.
                    if(jwtTokenProvider.validateToken(token)) {

                        /** 사용자 인증토큰 검사 */
                        Authentication auth = jwtTokenProvider.getAuthentication(token);  // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
                        SecurityContextHolder.getContext().setAuthentication(auth); // SecurityContext 에 Authentication 객체를 저장합니다.
                    }
                }
                filterChain.doFilter(request, response);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
