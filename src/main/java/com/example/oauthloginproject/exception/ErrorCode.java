package com.example.oauthloginproject.exception;

import lombok.*;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    NOT_NULL("ERROR_CODE_0001","필수값이 누락되었습니다")
    , NOT_BLANK("ERROR_CODE_0002", "필수값이 누락되었습니다.")
    ,EMAIL("ERROR_CODE_EMAIL", "이메일 형식이 아닙니다.")
    ;

    @Getter
    private String code;

    @Getter
    private String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
