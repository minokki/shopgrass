package com.grassshop.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {

    @NotBlank
    @Length(min=3, max=20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{3,20}$")
    private String nickname;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Length(min = 8, max=50)
    private String password;

    @NotBlank(message = "선택되지 않았습니다. 버튼을 선택하고 다시 진행해주세요.")
    private String userType;

}
