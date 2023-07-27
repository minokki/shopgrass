package com.grassshop.Cart;

import com.grassshop.repository.AccountRepository;
import com.grassshop.repository.CartRepository;
import com.grassshop.service.AccountService;
import com.grassshop.dto.SignUpForm;
import com.grassshop.entity.Account;
import com.grassshop.entity.Cart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartRepositoryTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Account createAccount() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setEmail("1234@gmail.com");
        signUpForm.setNickname("mino");
        signUpForm.setPassword("1234");
        signUpForm.setUserType("구매자");
        return accountService.saveNewAccount(signUpForm);
    }

    @Test
    @DisplayName("장바구니 회원 앤티티 매핑 조회 테스트")
    public void findCartAndAccount_Test(){
        Account account = createAccount();
        accountRepository.save(account);

        Cart cart = new Cart();
        cart.setAccount(account);
        cartRepository.save(cart);

        em.flush();
        em.clear();

        Cart savedCart = cartRepository.findById(cart.getId()).orElseThrow(EntityExistsException::new);
        Assertions.assertEquals(savedCart.getAccount().getId(),account.getId());


    }
}