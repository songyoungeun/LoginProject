package com.example.oauthloginproject.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter @Setter
@NoArgsConstructor
@Entity
public class Member {

    //id email pw name
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //자동 생성 id

    @Column(length = 100, nullable = false)
    private String email; // 사용자 이메일

    @Column(length = 200, nullable = false)
    private String password; // 사용자 비밀번호

    @Column(length = 100, nullable = false)
    private String name; // 사용자 이름


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate; // 생성일

    @UpdateTimestamp
    private LocalDateTime modifiedDate; //수정일


    @Builder
    public Member(Long id, String email,
                  String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

}
