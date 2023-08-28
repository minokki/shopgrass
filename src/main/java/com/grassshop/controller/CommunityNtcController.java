package com.grassshop.controller;

import com.grassshop.account.CurrentUser;
import com.grassshop.dto.BoardMainFormDto;
import com.grassshop.dto.NtcFormDto;
import com.grassshop.entity.Account;
import com.grassshop.repository.CommunityRepository;
import com.grassshop.service.CommunityNtcService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class CommunityNtcController {

    private final CommunityNtcService communityNtcService;

    @GetMapping(value = "/community/ntcs")
    public String getNtcs(@CurrentUser Account account, Model model){
        if(account != null){
            model.addAttribute(account);
        }
        return "community/community_ntc";
    }

    @GetMapping(value = "/admin/ntc/new")
    public String adminNtcForm(@CurrentUser Account account,Model model) {
        model.addAttribute("ntcFormDto", new NtcFormDto());
        model.addAttribute(account);
        return "community/community_ntc_form";
    }

    @PostMapping(value = "/admin/ntc/write")
    public String ntcNew(@Valid NtcFormDto ntcFormDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "community/community_ntc_form";
        }
        try {
            communityNtcService.saveCommunityNtc(ntcFormDto);
        }catch (Exception e){
            model.addAttribute("errorMessage","공지사항 작성중 에러발생");
            return "community/community_ntc_form";
        }
        return "redirect:/";
    }

}
