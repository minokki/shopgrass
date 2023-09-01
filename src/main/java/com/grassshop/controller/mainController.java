package com.grassshop.controller;

import com.grassshop.account.CurrentUser;
import com.grassshop.entity.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainController {

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model){
        if( account != null) {
            model.addAttribute(account);
        }
        return "index";
    }
    @GetMapping("/company/company_info")
    public String company_info(@CurrentUser Account account,Model model){
        if( account != null) {
            model.addAttribute(account);
        }
        return "company/company_info";
    }

    @GetMapping("/community/companyMap")
    public String company_map(@CurrentUser Account account, Model model) {
        if( account != null) {
            model.addAttribute(account);
        }
        return "community/community_companyMap";
    }



    @GetMapping("/login")
        public String login(){
            return "account/account_login";
        }
    }

