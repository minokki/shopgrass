package com.grassshop.repository;

import com.grassshop.constant.ItemStatus;
import com.grassshop.constant.OrderStatus;
import com.grassshop.dto.OrderSearchDto;
import com.grassshop.entity.Order;
import com.grassshop.entity.QItem;
import com.grassshop.entity.QOrder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public OrderRepositoryCustomImpl(EntityManager em) {
        this.queryFactory= new JPAQueryFactory(em);
    }
    private BooleanExpression searchOrderStatusEq(OrderStatus searchOrderStatus) {
        return searchOrderStatus == null ? null : QOrder.order.orderStatus.eq(searchOrderStatus);
    }
    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return QOrder.order.regTime.after(dateTime);
    }
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("orderId", searchBy)) {
            return QOrder.order.id.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QOrder.order.createBy.like("%" + searchQuery + "%");
        }
        return null;
    }
    @Override
    public Page<Order> getAdminOrderPage(OrderSearchDto orderSearchDto, Pageable pageable) {
        List<Order> content = queryFactory
                .selectFrom(QOrder.order)
                .where(regDtsAfter(orderSearchDto.getSearchDateType()),
                        searchOrderStatusEq(orderSearchDto.getOrderStatus()),
                        searchByLike(orderSearchDto.getSearchBy(), orderSearchDto.getSearchQuery()))
                .orderBy(QOrder.order.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QOrder.order)
                .where(regDtsAfter(orderSearchDto.getSearchDateType()),
                        searchOrderStatusEq(orderSearchDto.getOrderStatus()),
                        searchByLike(orderSearchDto.getSearchBy(), orderSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
