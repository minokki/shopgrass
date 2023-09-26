package com.grassshop.repository;

import com.grassshop.dto.MemberSearchDto;
import com.grassshop.entity.Account;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

public class AccountRepositoryCustomImpl implements AccountRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public AccountRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Account> getAdminMemberPage(MemberSearchDto memberSearchDto, Pageable pageable) {
        return null;
    }
}
