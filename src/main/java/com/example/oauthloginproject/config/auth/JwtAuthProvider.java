package com.example.oauthloginproject.config.auth;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtAuthProvider {

    private static final String BEARER_TYPE = "Bearer";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String ACCESS_USER_ID = "id";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L; //30분 //1000L * 60 * 60 * 12; //-> 12시간 , 60000 1분
//    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24; //하루
    @Value("${spring.jwt.secret.signature}")
    private String signatureKey;
//    private String signatureKey =  "TOYPROJECTSONGYOUNGEUNLOGINSERVICE";

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        signatureKey = Base64.getEncoder().encodeToString(signatureKey.getBytes());
    }

    private final UserDetailsService userDetailsService;

    //jwt 발급
    public String createAccessToken(Long id, String username, String role) {

        /**
         * access token 발급
         * 토큰 발급을 위한 데이터는 UserDetails에서 담당
         * UserDetails를 세부 구현한 CustomUSerDetails로 회원정보 전달
         */
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
//        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        final JwtBuilder builder = Jwts.builder()
                .setSubject(username) //accessToken
                .setExpiration(accessTokenExpiresIn)
                .claim(AUTHORITIES_KEY, role) // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.
                .claim(ACCESS_USER_ID, id)
                .signWith(SignatureAlgorithm.HS256, signatureKey);
        return BEARER_TYPE + builder.compact();

    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * @method 설명 : 컨텍스트에 해당 유저에 대한 권한을 전달하고 API 접근 전 접근 권한을 확인하여 접근 허용 또는 거부를 진행한다.
     */
    @SuppressWarnings("unchecked")
    public Authentication getAuthentication(String token) {

        // 토큰 기반으로 유저의 정보 파싱
        Claims claims = Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(token).getBody();

        String username = claims.getSubject();
        long id = claims.get(ACCESS_USER_ID, Integer.class);
        String role = claims.get(AUTHORITIES_KEY, String.class);

        CustomUserDetails userDetails = new CustomUserDetails(id, username, role);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * @method 설명 : request객체 헤더에 담겨 있는 토큰 가져오기
     */
    public String resolveToken(HttpServletRequest request) {
        if (request.getHeader("Authorization") == null)
            return null;

        return request.getHeader("Authorization").replace("Bearer", "");
    }

    /**
     * @method 설명 : 토큰 유효시간 만료여부 검사 실행
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
