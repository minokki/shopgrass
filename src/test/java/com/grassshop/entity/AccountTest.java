package com.grassshop.entity;

import com.grassshop.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
class AccountTest {
    @Autowired
    AccountRepository accountRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "mino" , roles = "USER")
    public void auditingTest(){
        Account account = new Account();
        account.setUserType("USER");
        accountRepository.save(account);



        Account account1 = accountRepository.findById(account.getId()).orElseThrow(EntityNotFoundException::new);

        System.out.println("RegTime : " + account1.getRegTime());
        System.out.println("getUpdateTime : " + account1.getUpdateTime());
        System.out.println("CreateBy : " + account1.getCreateBy());
        System.out.println("ModifiedBy : " + account1.getModifiedBy());
    }
}