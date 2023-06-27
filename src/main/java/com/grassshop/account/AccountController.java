package com.grassshop.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm") //해당 모델에 초기화 바인더 설정
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
       // addValidators() 메소드를 사용하여 유효성 검증기를 등록하면, 해당 폼 데이터의 유효성 검증을 수행할 수 있음
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute("SignUpForm",new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors){
        if(errors.hasErrors()){
            return "account/sign-up";
        }


        //todo 회원가입 처리
        return "redirect:/";
    }
}
