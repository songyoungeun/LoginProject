package com.example.oauthloginproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class AuthenticationDto {

    //id email pw name nickname
    private Long id;
    private String email;
    private String name;
    private String createdDate;
    private String modifiedDate;
}
