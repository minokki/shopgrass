package com.grassshop.controller;

import com.grassshop.account.CurrentUser;
import com.grassshop.entity.Account;
import com.grassshop.repository.AccountRepository;
import com.grassshop.service.AccountService;
import com.grassshop.validator.NicknameFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AccountService accountService;

    private final AccountRepository accountRepository;

    private final NicknameFormValidator nicknameValidator;

//    @InitBinder("passwordForm")
//    public void passwordInitBinder(WebDataBinder webDataBinder) {
//        webDataBinder.addValidators(new PasswordFormValidator());
//    }
//
//    @InitBinder("nicknameForm")
//    public void nicknameInitBinder(WebDataBinder webDataBinder) {
//        webDataBinder.addValidators(nicknameValidator);
//    }

    //프로필 수정
    @GetMapping("/admin_page/main")
    public String adminMainPage(@CurrentUser Account account, Model model) {
        model.addAttribute(account);

        return "admin/admin_main";
    }

//    @PostMapping("/settings/profile")
//    public String profileUpdateForm(@CurrentUser Account account, @Valid @ModelAttribute Profile profile, Errors errors,
//                                    Model model, RedirectAttributes attributes) {
//        if (errors.hasErrors()) {
//            model.addAttribute(account);
//            return "settings/setting_profile";
//        }
//        accountService.updateProfile(account, profile);
//        attributes.addFlashAttribute("message", "프로필을 수정했습니다");
//        return "redirect:" + "/settings/profile";
//    }
//
//    //패스워드 변경
//    @GetMapping("/settings/password")
//    public String passwordUpdateForm(@CurrentUser Account account, Model model) {
//        model.addAttribute(account);
//        model.addAttribute(new PasswordForm());
//        return "settings/setting_password";
//    }
//
//    @PostMapping("/settings/password")
//    public String passwordUpdate(@CurrentUser Account account, @Valid PasswordForm passwordForm,
//                                 Errors errors, Model model, RedirectAttributes attributes) {
//        if (errors.hasErrors()) {
//            model.addAttribute(account);
//            return "settings/setting_password";
//        }
//        accountService.updatePassword(account, passwordForm.getNewPassword());
//        attributes.addFlashAttribute("message", "패스워드를 변경했습니다");
//        return "redirect:" + "/settings/password";
//    }
//
//    //닉네임 변경
//    @GetMapping("/settings/nickname")
//    public String nicknameUpdateForm(@CurrentUser Account account, Model model) {
//        model.addAttribute(account);
//        model.addAttribute(new NicknameForm());
//        return "settings/setting_nickname";
//    }
//
//    @PostMapping("/settings/nickname")
//    public String nicknameUpdate(@CurrentUser Account account, @Valid NicknameForm nicknameForm, Errors errors,
//                                 Model model, RedirectAttributes attributes) {
//        if (errors.hasErrors()) {
//            model.addAttribute(account);
//            return "settings/setting_nickname";
//        }
//        accountService.updateNickname(account, nicknameForm.getNickname());
//        attributes.addFlashAttribute("message", "닉네임을 수정했습니다.");
//        return "redirect:" + "/settings/nickname";
//    }
}
