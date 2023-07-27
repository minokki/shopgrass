package com.grassshop.main;

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

    @GetMapping("/login")
        public String login(){
            return "login";
        }
    }

