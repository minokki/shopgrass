package com.grassshop.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of="id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private  String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emilVerified;

    private String emailCheckToken;

    private LocalDateTime joinedAt;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    @Lob @Basic(fetch = FetchType.LAZY)  //lob = 텍스트 타입
    private String profileImage;

    private boolean shopCreateByEmail;

    private boolean shopCreateByWeb;

    private boolean shopEnrollmentResultByEmail;

    private boolean shopEnrollmentResultByWeb;

    private boolean shopUpdatedByEmail;

    private boolean shopUpdatedByWeb;


    public void generateEmailCheckToken() {
        this.emailCheckToken= UUID.randomUUID().toString();
    }
}
