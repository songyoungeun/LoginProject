package com.example.oauthloginproject.dto;

import com.example.oauthloginproject.domain.Member;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter @Setter
public class LoginDto {

    //id email pw name nickname
    //entity로 데이터 넣어주기
    // email pw 예외처리 해주기
    @NotBlank(message = "이메일은 필수 입력 값 입니다.") //null 과 "" 과 " " 모두 허용하지 않습니다.
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
    private String password;

    public Member toEntity() {
        Member build = Member.builder()
                        .email(email)
                        .password(password)
                        .build();
        return build;
    }
}
