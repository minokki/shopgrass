package com.grassshop.controller;

import com.grassshop.account.CurrentUser;
import com.grassshop.entity.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainController {

    /* 메인페이지 이동 */
    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model){
        if( account != null) {
            model.addAttribute(account);
        }
        return "index";
    }

    /* 회사소개 페이지 이동 */
    @GetMapping("/company/company_info")
    public String company_info(@CurrentUser Account account,Model model){
        if( account != null) {
            model.addAttribute(account);
        }
        return "company/company_info";
    }

    /* 오시는길 페이지 이동 */
    @GetMapping("/community/companyMap")
    public String company_map(@CurrentUser Account account, Model model) {
        if( account != null) {
            model.addAttribute(account);
        }
        return "community/community_companyMap";
    }

    /* 로그인페이지 이동  */
    @GetMapping("/login")
        public String login(){
            return "account/account_login";
        }
    }

