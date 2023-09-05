package com.grassshop.controller;

import com.grassshop.dto.NtcFormDto;
import com.grassshop.dto.QnaFormDto;
import com.grassshop.dto.QnaSearchDto;
import com.grassshop.entity.Ntc;
import com.grassshop.entity.Qna;
import com.grassshop.service.QnaService;
import lombok.Getter;
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
public class QnaController {

    private final QnaService qnaService;


    @GetMapping(value = {"/community/qnas", "/community/qnas/{page}"})
    public String qnaManage(QnaSearchDto qnaSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Qna> qnas = qnaService.getQnaPage(qnaSearchDto, pageable);
        model.addAttribute("qnas", qnas);
        model.addAttribute("qnaSearchDto", qnaSearchDto);
        model.addAttribute("maxPage",5);
        return "community/community_qna";
    }

    @GetMapping(value = "/qna/new")
    public String qnaForm(Model model) {
        model.addAttribute("qnaFormDto", new QnaFormDto());
        return "community/community_qna_form";
    }

    @PostMapping(value = "/qna/write")
    public String qnaWrite(@Valid QnaFormDto qnaFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "community/community_qna_form";
        }
        try {
            qnaService.saveCommunityQna(qnaFormDto);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Q&A 작성중 에러발생");
            return "community/community_qna_form";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/qna/{qnaId}")
    public String getQnaForm(@PathVariable("qnaId") Long qnaId, Model model) {
        try {
            QnaFormDto qnaFormDto = qnaService.getCommunityQna(qnaId);
            model.addAttribute("qnaFormDto", qnaFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지않는 게시글");
            model.addAttribute("ntcFormDto", new NtcFormDto());
            return "community/community_qna_form";
        }
        return "community/community_qna_form";
    }

    @PostMapping(value = "/qna/{qnaId}")
    public String qnaUpdate(@Valid QnaFormDto qnaFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "community/community_qna_form";
        }
        try {
            qnaService.updateCommunityQna(qnaFormDto);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 수정시 에러 발생");
        }
        return "redirect:/";
    }

    @GetMapping(value = "/community/qna/{qnaId}")
    public String getQnaDtl(@PathVariable("qnaId") Long qnaId, Model model) {
        QnaFormDto qnaFormDto = qnaService.getCommunityQna(qnaId);
        model.addAttribute("qna", qnaFormDto);
        return "community/community_qna_detail";
    }
}
