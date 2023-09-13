package com.grassshop.controller;

import com.grassshop.account.CurrentUser;
import com.grassshop.dto.ItemFormDto;
import com.grassshop.dto.ItemSearchDto;
import com.grassshop.dto.MainItemDto;
import com.grassshop.entity.Account;
import com.grassshop.entity.Item;
import com.grassshop.service.ItemService;
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
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /* 상품 SAVE(GET) */
    @GetMapping(value = "/admin/item/new")
    public String itemForm(@CurrentUser Account account, Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        model.addAttribute(account);
        return "item/item_form";
    }

    /* 상품 SAVE(POST)*/
    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        if (bindingResult.hasErrors()) {
            return "item/item_form";
        }
        List<MultipartFile> validItemImgFileList = itemImgFileList.stream()  //업로드된 이미지만 변수에 저장
                .filter(file -> file.getSize() > 0)
                .collect(Collectors.toList());

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 이미지는 필수값");
            return "item/item_form";
        }
        try {
            itemService.saveItem(itemFormDto, validItemImgFileList); //상품 저장 로직
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품등록중 에러가 발생했습니다");
            return "item/item_form";
        }
        return "redirect:/";
    }

    /* 상품 UPDATE(GET) */
    @GetMapping(value = "admin/item/{itemId}")
    public String itemDtl(@CurrentUser Account account, @PathVariable("itemId") Long itemId, Model model) {
        try {
            model.addAttribute(account);
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/item_form";
        }
        return "item/item_form";
    }

    /* 상품 UPDATE(POST) */
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {
        if (bindingResult.hasErrors()) {
            return "item/item_form";
        }
        List<MultipartFile> validItemImgFileList = itemImgFileList.stream()  //업로드된 이미지만 변수에 저장
                .filter(file -> file.getSize() > 0)
                .collect(Collectors.toList());
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/item_form";
        }
        try {
            itemService.updateItem(itemFormDto, validItemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/item_form";
        }
        return "redirect:/";
    }

    /* 상품 목록(ADMIN) */
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(@CurrentUser Account account, ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute(account);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        return "item/item_mng";
    }

    /* 상품목록 */
    @GetMapping(value = "/item/items")
    public String getItemMain(@CurrentUser Account account, ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        if( account != null) {
            model.addAttribute(account);
        }

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/item_main";
    }

    /* 상품 DETAIL */
    @GetMapping(value = "/item/{itemId}")
    public String getItemDetail(@CurrentUser Account account, Model model, @PathVariable("itemId") Long itemId) {
        if( account != null) {
            model.addAttribute(account);
        }

        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/item_detail";
    }
}
