package com.grassshop.settings;

import com.grassshop.WithAccount;
import com.grassshop.account.AccountRepository;
import com.grassshop.domain.Account;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @AfterEach
    void afterEach(){
        accountRepository.deleteAll();
    }

    @WithAccount("mino")
    @DisplayName("프로필 수정 폼")
    @Test
    void updateProfileForm() throws Exception {
        mockMvc.perform(get("/settings/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }
    @WithAccount("mino")
    @DisplayName("프로필 수정 - 입력값 정상")
    @Test
    void updateProfile() throws Exception {
        mockMvc.perform(post("/settings/profile")
                        .param("bio", "짧은 소개를 하는경우")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("message"));

        Account account = accountRepository.findByNickname("mino");
        Assertions.assertEquals("짧은 소개를 하는경우",account.getBio());
    }

    @WithAccount("mino")
    @DisplayName("프로필 수정 - 입력값 에러")
    @Test
    void updateProfile_error() throws Exception {
        String bio = "35자이상 작성한경우에러, 35자이상 작성한경우에러,35자이상 작성한경우에러,35자이상 작성한경우에러,35자이상 작성한경우에러,";
        mockMvc.perform(post("/settings/profile")
                        .param("bio", bio)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("settings/profile"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));

        Account account = accountRepository.findByNickname("mino");
        Assertions.assertNull(account.getBio());
    }

    @WithAccount("mino")
    @DisplayName("프로필 수정 폼")
    @Test
    void updatePasswordForm() throws Exception {
        mockMvc.perform(get("/settings/password"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"));
    }
    @WithAccount("mino")
    @DisplayName("패스워드 수정- 정상")
    @Test
    void updatePassword() throws Exception {
        String newPassword = "00000000";
        String newPasswordConfirm = "00000000";
        mockMvc.perform(post("/settings/password")
                        .param("newPassword", newPassword)
                        .param("newPasswordConfirm",newPasswordConfirm)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/password"))
                .andExpect(flash().attributeExists("message"));

        Account account = accountRepository.findByNickname("mino");
        Assertions.assertTrue(passwordEncoder.matches("00000000",account.getPassword()));
    }

    @WithAccount("mino")
    @DisplayName("패스워드 수정 - 오류")
    @Test
    void updatePassword_Error() throws Exception {
        String newPassword = "00000000";
        String newPasswordConfirm = "00000001";
        mockMvc.perform(post("/settings/password")
                        .param("newPassword", newPassword)
                        .param("newPasswordConfirm", newPasswordConfirm)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("settings/password"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(model().attributeExists("account"));
    }
}