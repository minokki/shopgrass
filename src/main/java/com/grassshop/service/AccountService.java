package com.grassshop.service;

import com.grassshop.repository.AccountRepository;
import com.grassshop.account.UserAccount;
import com.grassshop.config.AppProperties;
import com.grassshop.entity.Account;
import com.grassshop.constant.Role;
import com.grassshop.dto.SignUpForm;
import com.grassshop.mail.EmailMessage;
import com.grassshop.mail.EmailService;
import com.grassshop.dto.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;

    /* 회원가입 토큰생성 */
    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm); //아래 메서드는 영속성 컨텍스트 상태임. 그 정보를 가지고 토큰 생성후 저장하려면, 트랜젝션 어노테이션 작성해줘야함
        //이메일 체크토큰 생성
        newAccount.generateEmailCheckToken();
//        sentConfirmEmail(newAccount);
        return newAccount;
    }

    /* 회원가입 */
    public Account saveNewAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword())) //패스워드 인코드
                .role(Role.USER)
                .userType(signUpForm.getUserType())
                .build();
        Account newAccount = accountRepository.save(account);  //회원저장
        return newAccount;
    }

    /* 인증 이메일 보내기 */
    public void sentConfirmEmail(Account newAccount) {
        Context context = new Context();
        context.setVariable("link","/check-email-token?token=" + newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());
        context.setVariable("nickname",newAccount.getNickname());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message","벌초박사 서비스를 사용하려면 링크를 클릭하세요.");
        context.setVariable("host",appProperties.getHost());
        String message = templateEngine.process("mail/email_link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("벌초박사, 회원가입 인증")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
    }

    /* 로그인 */
    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account), //사용자 정보
                account.getPassword(), //사용자 패스워드
                new UserAccount(account).getAuthorities()); // 여기서 authorities 메서드를 이용하여 권한 추가
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    /* ??? */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);
        if(account == null){
            account = accountRepository.findByNickname(emailOrNickname);
        }

        if(account == null){
            throw new UsernameNotFoundException(emailOrNickname);
        }

        return new UserAccount(account);
    }

    /* 회원가입시 자동로그인 */
    public void completeSingUP(Account account) {
        account.completeSignUp();
        login(account);
    }

    /* 프로필 UPDATE */
    public void updateProfile(Account account, Profile profile) {
        account.setUrl(profile.getUrl());
        account.setOccupation(profile.getOccupation());
        account.setLocation(profile.getLocation());
        account.setBio(profile.getBio());
        account.setProfileImage(profile.getProfileImage());
        accountRepository.save(account);
    }

    /* 프로필 PASSWORD */
    public void updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }
    /* 프로필 NICKNAME */
    public void updateNickname(Account account, String nickname) {
        account.setNickname(nickname);
        accountRepository.save(account);
        login(account);
    }

    /* 이메일-로그인링크 */
    public void sendLoginLing(Account account) {
        Context context = new Context();
        context.setVariable("link","/login-by-email?token=" + account.getEmailCheckToken() + "&email=" + account.getEmail());
        context.setVariable("nickname",account.getNickname());
        context.setVariable("linkName", "이메일로 로그인하기");
        context.setVariable("message","로그인 하려면 아래 링크를 클릭하세요.");
        context.setVariable("host",appProperties.getHost());
        String message = templateEngine.process("mail/email_link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("벌초박사, 로그인 링크")
                .message(message)
                        .build();

        emailService.sendEmail(emailMessage);

    }
}
