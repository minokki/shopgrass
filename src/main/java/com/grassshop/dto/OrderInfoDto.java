package com.grassshop.dto;

import com.grassshop.constant.OrderStatus;
import com.grassshop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrderInfoDto {
    private Long orderId; //주문아이디

    private String orderDate; //주문 날짜

    private OrderStatus orderStatus; //주문상태

    public OrderInfoDto(Order order) {
        this.orderId=order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus=order.getOrderStatus();
    }

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
    }
}
