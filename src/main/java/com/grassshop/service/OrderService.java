package com.grassshop.service;

import com.grassshop.dto.OrderDto;
import com.grassshop.entity.Account;
import com.grassshop.entity.Item;
import com.grassshop.entity.Order;
import com.grassshop.entity.OrderItem;
import com.grassshop.repository.AccountRepository;
import com.grassshop.repository.ItemRepository;
import com.grassshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
