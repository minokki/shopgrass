package com.grassshop.controller;

import com.grassshop.account.CurrentUser;
import com.grassshop.dto.BoardMainFormDto;
import com.grassshop.dto.BoardSearchDto;
import com.grassshop.dto.MainBoardDto;
import com.grassshop.entity.Account;
import com.grassshop.entity.BoardMain;
import com.grassshop.service.BoardMainService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardMainController {

    private final BoardMainService boardMainService;

    @GetMapping(value = "/boardMain/info")
    public String boardMainInfo(@CurrentUser Account account, Model model){
        if( account != null) {
            model.addAttribute(account);
        }
        return "boardMain/board_info";
    }

    @GetMapping(value = "/boardMain/new")
    public String boardMainForm(@CurrentUser Account account,Model model) {
        model.addAttribute("boardMainFormDto", new BoardMainFormDto());
        model.addAttribute(account);
        return "boardMain/board_form";
    }

    @PostMapping(value = "/admin/boardMain/write")
    public String boardMainNew(@Valid BoardMainFormDto boardMainFormDto, BindingResult bindingResult, Model model,
                               @RequestParam("boardMainImgFile") List<MultipartFile> multipartFiles) {
        if (bindingResult.hasErrors()) {
            return "boardMain/board_form";
        }

        if (multipartFiles.get(0).isEmpty() && boardMainFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 이미지는 필수 입력");
            return "boardMain/board_form";
        }

        try {

            boardMainService.saveBoardMain(boardMainFormDto, multipartFiles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품등록중 에러 발생");
            return "boardMain/board_form";
        }
        return "redirect:/";

    }

    @GetMapping("/admin/boardMain/{boardMainId}")
    public String boardMainDtl(@CurrentUser Account account, @PathVariable("boardMainId") Long boardMainId, Model model) {
        try {
            BoardMainFormDto boardMainFormDto = boardMainService.getBoardMainDtl(boardMainId);
            model.addAttribute(account);
            model.addAttribute("boardMainFormDto", boardMainFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 게시글");
            model.addAttribute("boardMainFormDto", new BoardMainFormDto());
            return "boardMain/board_form";
        }
        return "boardMain/board_form";
    }

    @PostMapping("/admin/boardMain/{boardMainId}")
    public String boardMainUpdate(@Valid BoardMainFormDto boardMainFormDto, BindingResult bindingResult,
                                  @RequestParam("boardMainImgFile") List<MultipartFile> multipartFiles, Model model) {

        if (bindingResult.hasErrors()) {
            return "boardMain/board_form";
        }

        if (multipartFiles.get(0).isEmpty() && boardMainFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 이미지는 필수값");
            return "boardMain/board_form";
        }

        try {
            boardMainService.updateBoardMain(boardMainFormDto, multipartFiles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 수정중 에러 발생");
            return "boardMain/board_form";
        }
        return "redirect:/boardMain/{boardMainId}";
    }

    @GetMapping(value = {"/admin/boardMains", "/admin/boardMains/{page}"})
    public String boardMainManage(@CurrentUser Account account, BoardSearchDto boardSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<BoardMain> boardMains = boardMainService.getAdminBoardMainPage(boardSearchDto, pageable);
        model.addAttribute(account);
        model.addAttribute("boardMains", boardMains);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 5);
        return "boardMain/board_mng";
    }

    @GetMapping(value = "/boardMain/boardMains")
    public String getBoardMain(@CurrentUser Account account, BoardSearchDto boardSearchDto, Optional<Integer> page, Model model) {
        if( account != null) {
            model.addAttribute(account);
        }
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 12);
        Page<MainBoardDto> boardMains = boardMainService.getMainBoardPage(boardSearchDto, pageable);
        model.addAttribute("boardMains", boardMains);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 5);

        return "boardMain/board_main";
    }

    @GetMapping(value = "/boardMain/{boardMainId}")
    public String boardMainDtl(@CurrentUser Account account, Model model, @PathVariable("boardMainId") Long boardMainId) {
        if( account != null) {
            model.addAttribute(account);
        }
        BoardMainFormDto boardMainFormDto = boardMainService.getBoardMainDtl(boardMainId);
        model.addAttribute("boardMain", boardMainFormDto);
        return "boardMain/board_detail";
    }
}
