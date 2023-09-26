package com.grassshop.controller;

import com.grassshop.account.CurrentUser;
import com.grassshop.dto.BoardSearchDto;
import com.grassshop.dto.ItemSearchDto;
import com.grassshop.dto.MemberSearchDto;
import com.grassshop.dto.OrderSearchDto;
import com.grassshop.entity.Account;
import com.grassshop.entity.BoardMain;
import com.grassshop.entity.Item;
import com.grassshop.entity.Order;
import com.grassshop.service.AccountService;
import com.grassshop.service.BoardMainService;
import com.grassshop.service.ItemService;
import com.grassshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AccountService accountService;

    private final OrderService orderService;

    private final BoardMainService boardMainService;

    private final ItemService itemService;

    /* 회원목록 */
    @GetMapping({"/admin/members","/admin/members/{page}"})
    public String adminMainPage(@CurrentUser Account account,MemberSearchDto memberSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        model.addAttribute(account);
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<Account> members = accountService.getAdminMemberPage(memberSearchDto, pageable);
        model.addAttribute("members",members);
        model.addAttribute("memberSearchDto", memberSearchDto);
        model.addAttribute("maxPage", 5);
        

        return "admin/admin_members";
    }

    /* 주문목록 보기 */
    @GetMapping(value = {"/admin/orders","/admin/orders/{page}"})
    public String orderManage(@CurrentUser Account account, OrderSearchDto orderSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<Order> orders = orderService.getAdminOrderPage(orderSearchDto,pageable);
        model.addAttribute(account);
        model.addAttribute("orders", orders);
        model.addAttribute("orderSearchDto", orderSearchDto);
        model.addAttribute("maxPage", 5);

        return "order/order_mng";
    }

    /* 시공사례 목록(ADMIN) */
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

}
