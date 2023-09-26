package com.grassshop.repository;

import com.grassshop.entity.Account;
import com.grassshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long>, QuerydslPredicateExecutor<Account>, AccountRepositoryCustom{
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Account findByEmail(String email);
    Account findByNickname(String nickname);
}
