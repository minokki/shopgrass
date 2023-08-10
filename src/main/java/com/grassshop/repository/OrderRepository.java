package com.grassshop.repository;

import com.grassshop.dto.OrderSearchDto;
import com.grassshop.entity.Item;
import com.grassshop.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, QuerydslPredicateExecutor<Order>, OrderRepositoryCustom {
    @Query("select o from Order o " +
            "where o.account.email= :email " +
            "order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    @Query("select count(o) from Order o " + "where o.account.email = :email")
    Long countOrder(@Param("email") String email);

}
