package com.grassshop.controller;

import com.grassshop.account.CurrentUser;
import com.grassshop.dto.*;
import com.grassshop.entity.Account;
import com.grassshop.entity.Qna;
import com.grassshop.service.QnaService;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    /* 게시글 목록 */
    @GetMapping(value = {"/community/qnas", "/community/qnas/{page}"})
    public String qnaManage(QnaSearchDto qnaSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Qna> qnas = qnaService.getQnaPage(qnaSearchDto, pageable);
        model.addAttribute("qnas", qnas);
        model.addAttribute("qnaSearchDto", qnaSearchDto);
        model.addAttribute("maxPage",5);
        return "community/community_qna";
    }
    /* Q&A 작성(GET) */
    @GetMapping(value = "/qna/new")
    public String qnaForm(Model model) {
        model.addAttribute("qnaFormDto", new QnaFormDto());
        return "community/community_qna_form";
    }

    /* Q&A 작성(POST) */
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
        return "redirect:/community/qnas";
    }

    /* Q&A 수정(GET) */
    @GetMapping(value = "/qna/{qnaId}")
    public String getQnaForm(@PathVariable("qnaId") Long qnaId, Model model) {
        try {
            QnaFormDto qnaFormDto = qnaService.getCommunityQna(qnaId);
            model.addAttribute("qnaFormDto", qnaFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지않는 게시글");
            model.addAttribute("qnaFormDto", new QnaFormDto());
            return "community/community_qna_form";
        }
        return "community/community_qna_form";
    }

    /* Q&A 수정(POST) */
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
        return "redirect:/community/qna/{qnaId}";
    }

    /* Q&A(detail) */
    @GetMapping(value = "/community/qna/{qnaId}")
    public String getQnaDtl(@PathVariable("qnaId") Long qnaId, @CurrentUser Account account, Model model) {
        if(account != null){
            model.addAttribute(account);
        }
        QnaResponseDto dto = qnaService.getQnaById(qnaId);
        /* 조회수 */
        Qna qna = qnaService.viewQna(qnaId);
        List<CommentResponseDto> comments = dto.getComments();
        /* 댓글 */
        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
            model.addAttribute("commentCount", comments.size());
        } else {
            model.addAttribute("commentCount", 0);
        }
        model.addAttribute("qnaViews", qna);
        model.addAttribute("qna", dto);

        return "community/community_qna_detail";
    }


}
