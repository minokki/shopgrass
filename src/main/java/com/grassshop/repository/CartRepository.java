package com.grassshop.repository;


import com.grassshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByAccountId(Long accountId);

}
