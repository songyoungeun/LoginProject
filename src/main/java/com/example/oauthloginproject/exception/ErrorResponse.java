package com.example.oauthloginproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse { //API에 내려줄 에러 형태를 정하였다

    private String code; //에러 코드

    private String description; //어떤 에러인지

    private String detail; //에러 세부사항

    public ErrorResponse(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public ErrorResponse(String code, String description, String detail) {
        this.code = code;
        this.description = description;
        this.detail = detail;
    }


}
