package com.example.oauthloginproject.config.auth;

import com.example.oauthloginproject.domain.Member;
import com.example.oauthloginproject.exception.custom.UserNotFoundException;
import com.example.oauthloginproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member entity = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 회원의 정보입니다."));

        UserDetails member = new CustomUserDetails(entity.getId(), email, "USER");
        return member;
    }
}
