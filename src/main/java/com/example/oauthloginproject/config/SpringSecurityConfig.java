package com.example.oauthloginproject.config;

import com.example.oauthloginproject.config.auth.CustomAccessDenied;
import com.example.oauthloginproject.config.auth.CustomAuthenticationEntry;
import com.example.oauthloginproject.config.auth.JwtAuthProvider;
import com.example.oauthloginproject.config.auth.CustomFilter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthProvider jwtAuthProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setMaxAge((long) 3600);
        configuration.setAllowCredentials(false);
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("content-disposition");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("*/h2-console/**"))
                .and().headers().addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy","script-src 'self'")).frameOptions().disable();

        http
                .httpBasic().disable()
                .csrf().disable() // ????????? ??? ?????? ??????
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt ?????? ?????? ?????? ?????? ???????????? ??????
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() //Preflight ????????? ??????
                .antMatchers("/api/signin/**").permitAll() 		// ????????? --> api/signin : path ?????? ????????? ?????? 404 ?????? ???
                .antMatchers("/exception/**").permitAll() 	// ???????????? ?????????
                .anyRequest().hasRole("USER")				// ?????? ???????????? USER ????????????//                .anyRequest().permitAll()
//                .anyRequest().authenticated() //??? ??? ????????? ???????????? ????????? ???????????? ????????? ??? ??????.
                .and()
                .cors()
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDenied())// ??????????????? ??????
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntry())// ??????????????? ??????
                .and()
                .addFilterBefore(new CustomFilter(jwtAuthProvider), UsernamePasswordAuthenticationFilter.class);
                //  UsernamePasswordAuthenticationFilter ????????? CustomFilter??? ????????????
                //UsernamePasswordAuthenticationFilter : ???????????? ??????????????? ???????????? form ?????? ??????
                //????????? ????????? url??? ?????? ?????? ??????, ?????? ?????? ??????
    }
}