package com.grassshop.controller;

import com.grassshop.account.CurrentUser;
import com.grassshop.account.UserAccount;
import com.grassshop.dto.OrderDto;
import com.grassshop.dto.OrderInfoDto;
import com.grassshop.dto.OrderSearchDto;
import com.grassshop.entity.Account;
import com.grassshop.entity.Order;
import com.grassshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class OrderController {
    private final OrderService orderService;

    /* 주문 */
    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                stringBuilder.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(stringBuilder.toString(), HttpStatus.BAD_REQUEST);
        }

        UserAccount userDetails = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getAccount().getEmail(); // 사용자의 이메일을 가져옵니다.
        Long orderId;
        try {
            orderId = orderService.order(orderDto, email);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);

    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderInfo(@CurrentUser Account account,@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {

        UserAccount userDetails = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //사용자 정보
        String email = userDetails.getAccount().getEmail(); // 사용자의 이메일을 가져옵니다.

        PageRequest pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<OrderInfoDto> orderInfoDtos = orderService.getOrderList(email, pageable);
        model.addAttribute(account);
        model.addAttribute("orders", orderInfoDtos);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);
        return "order/order_info";
    }

    @PostMapping(value = "/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
        UserAccount userDetails = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //사용자 정보
        String email = userDetails.getAccount().getEmail(); // 사용자의 이메일을 가져옵니다.
        if (!orderService.validateOrder(orderId, email)) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

}

