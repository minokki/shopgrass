package com.grassshop.repository;

import com.grassshop.dto.MemberSearchDto;
import com.grassshop.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountRepositoryCustom {
    Page<Account> getAdminMemberPage(MemberSearchDto memberSearchDto, Pageable pageable);
}
