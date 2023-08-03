package com.grassshop.controller;

import com.grassshop.account.CurrentUser;
import com.grassshop.dto.BoardMainFormDto;
import com.grassshop.dto.BoardSearchDto;
import com.grassshop.dto.MainBoardDto;
import com.grassshop.entity.Account;
import com.grassshop.entity.BoardMain;
import com.grassshop.service.BoardMainService;
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

    @GetMapping(value = "/boardMain/new")
    public String boardMainForm(Model model) {
        model.addAttribute("boardMainFormDto", new BoardMainFormDto());
        return "boardMain/boardMainForm";
    }

    @PostMapping(value = "/admin/boardMain/new")
    public String boardMainNew(@Valid BoardMainFormDto boardMainFormDto, BindingResult bindingResult, Model model,
                               @RequestParam("boardMainImgFile") List<MultipartFile> multipartFiles) {
        if (bindingResult.hasErrors()) {
            return "boardMain/boardMainForm";
        }

        if (multipartFiles.get(0).isEmpty() && boardMainFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 이미지는 필수 입력");
        }

        try {
            boardMainService.saveBoardMain(boardMainFormDto, multipartFiles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품등록중 에러 발생");
            return "boardMain/boardMainForm";
        }
        return "redirect:/";

    }

    @GetMapping("/admin/boardMain/{boardMainId}")
    public String boardMainDtl(@PathVariable("boardMainId") Long boardMainId, Model model) {
        try {
            BoardMainFormDto boardMainFormDto = boardMainService.getBoardMainDtl(boardMainId);
            model.addAttribute("boardMainFormDto", boardMainFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 게시글");
            model.addAttribute("boardMainFormDto", new BoardMainFormDto());
            return "boardMain/boardMainForm";
        }
        return "boardMain/boardMainForm";
    }

    @PostMapping("/admin/boardMain/{boardMainId}")
    public String boardMainUpdate(@Valid BoardMainFormDto boardMainFormDto, BindingResult bindingResult,
                                  @RequestParam("boardMainImgFile") List<MultipartFile> multipartFiles, Model model) {

        if (bindingResult.hasErrors()) {
            return "boardMain/boardMainForm";
        }

        if (multipartFiles.get(0).isEmpty() && boardMainFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 이미지는 필수값");
            return "boardMain/boardMainForm";
        }

        try {
            boardMainService.updateBoardMain(boardMainFormDto, multipartFiles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 수정중 에러 발생");
            return "boardMain/boardMainForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = {"/admin/boardMains", "/admin/boardMains/{page}"})
    public String boardMainManage(BoardSearchDto boardSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        Page<BoardMain> boardMains = boardMainService.getAdminBoardMainPage(boardSearchDto, pageable);
        model.addAttribute("boardMains", boardMains);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 5);
        return "boardMain/boardMainMng";
    }

    @GetMapping(value = "/boardMain/boardMains")
    public String getBoardMain(BoardSearchDto boardSearchDto, Optional<Integer> page, Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 12);
        Page<MainBoardDto> boardMains = boardMainService.getMainBoardPage(boardSearchDto, pageable);

        model.addAttribute("boardMains", boardMains);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 5);

        return "boardMain/boardMain";
    }
}
