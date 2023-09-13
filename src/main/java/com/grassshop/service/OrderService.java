package com.grassshop.service;

import com.grassshop.dto.OrderDto;
import com.grassshop.dto.OrderInfoDto;
import com.grassshop.dto.OrderItemDto;
import com.grassshop.dto.OrderSearchDto;
import com.grassshop.entity.*;
import com.grassshop.repository.AccountRepository;
import com.grassshop.repository.ItemImgRepository;
import com.grassshop.repository.ItemRepository;
import com.grassshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;
    private final AccountRepository accountRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    /* 주문 SAVE */
    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        Account account = accountRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(account, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    /* 주문 READ */
    @Transactional(readOnly = true)
    public Page<OrderInfoDto> getOrderList(String email, Pageable pageable) {
        List<Order> orderByEmail = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderInfoDto> orderInfoDtoList = new ArrayList<>();

        for (Order order : orderByEmail) {
            OrderInfoDto orderInfoDto = new OrderInfoDto(order);
            List<OrderItem> orderItemList = order.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {
                ItemImg y = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, y.getImgUrl());
                orderInfoDto.addOrderItemDto(orderItemDto);
            }
            orderInfoDtoList.add(orderInfoDto);
        }

        return new PageImpl<OrderInfoDto>(orderInfoDtoList, pageable, totalCount);
    }

    /* 주문 VALIDATE */
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {
        Account byEmail = accountRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Account savedAccount = order.getAccount();

        if (!StringUtils.equals(byEmail.getEmail(), savedAccount.getEmail())) {
            return false;
        }
        return true;
    }

    /* 주문 CANCEL */
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    /* 주문 PAGING(ADMIN) */
    @Transactional(readOnly = true)
    public Page<Order> getAdminOrderPage(OrderSearchDto orderSearchDto, Pageable pageable) {
        return orderRepository.getAdminOrderPage(orderSearchDto, pageable);
    }
}
