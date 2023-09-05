package com.grassshop.controller;

import com.grassshop.account.CurrentUser;
import com.grassshop.dto.NtcFormDto;
import com.grassshop.dto.NtcSearchDto;
import com.grassshop.entity.Account;
import com.grassshop.entity.Ntc;
import com.grassshop.service.NtcService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class NtcController {

    private final NtcService ntcService;

    //공지사항 페이지 이동
    @GetMapping(value = {"/community/ntcs", "/community/ntcs/{page}"})
    public String ntcManage(@CurrentUser Account account, NtcSearchDto ntcSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        if(account != null){
            model.addAttribute(account);
        }

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Ntc> ntcs = ntcService.getNtcPage(ntcSearchDto, pageable);
        model.addAttribute("ntcs", ntcs);
        model.addAttribute("ntcSearchDto", ntcSearchDto);
        model.addAttribute("maxPage",5);
        return "community/community_ntc";

    }
    //글작성 페이지 이동
    @GetMapping(value = "/admin/ntc/new")
    public String adminNtcForm(@CurrentUser Account account,Model model) {
        model.addAttribute("ntcFormDto", new NtcFormDto());
        model.addAttribute(account);
        return "community/community_ntc_form";
    }
    //글작성 post
    @PostMapping(value = "/admin/ntc/write")
    public String ntcWrite(@Valid NtcFormDto ntcFormDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "community/community_ntc_form";
        }
        try {
            ntcService.saveCommunityNtc(ntcFormDto);
        }catch (Exception e){
            model.addAttribute("errorMessage","공지사항 작성중 에러발생");
            return "community/community_ntc_form";
        }
        return "redirect:/community/ntcs";
    }

    //게시글 불러오기(수정)
    @GetMapping(value = "/admin/ntc/{ntcId}")
    public String getNtcForm(@PathVariable("ntcId") Long ntcId, Model model){
        try {
            NtcFormDto communityNtc = ntcService.getCommunityNtc(ntcId);
            model.addAttribute("ntcFormDto", communityNtc);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 게시글입니다.");
            model.addAttribute("ntcFormDto", new NtcFormDto());
            return "community/community_ntc_form";
        }
        return "community/community_ntc_form";
    }

    //게시글 수정 post
    @PostMapping(value = "/admin/ntc/{ntcId}")
    public String ntcUpdate(@Valid NtcFormDto ntcFormDto, BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()) {
            return "community/community_ntc_form";
        }
        try {
            ntcService.updateCommunityNtc(ntcFormDto);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 수정중 에러 발생");
            return "community/community_ntc_form";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/community/ntc/{ntcId}")
    public String getNtcDtl(Model model, @PathVariable("ntcId") Long ntcId) {
        NtcFormDto ntcFormDto = ntcService.getCommunityNtc(ntcId);
        model.addAttribute("ntc", ntcFormDto);
        return "community/community_ntc_detail";
    }



}

