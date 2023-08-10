package com.grassshop.repository;

import com.grassshop.dto.OrderSearchDto;
import com.grassshop.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> getAdminOrderPage(OrderSearchDto orderSearchDto, Pageable pageable);
}
