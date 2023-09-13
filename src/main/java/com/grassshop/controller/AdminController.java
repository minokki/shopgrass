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

    /* 관리자 페이지 이동 */
    @GetMapping("/admin_page/main")
    public String adminMainPage(@CurrentUser Account account, Model model) {
        model.addAttribute(account);

        return "admin/admin_main";
    }


}
