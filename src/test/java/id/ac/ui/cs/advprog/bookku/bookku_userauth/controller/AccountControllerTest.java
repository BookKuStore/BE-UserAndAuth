package id.ac.ui.cs.advprog.bookku.bookku_userauth.controller;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.service.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc
class AccountControllerTest {

    private String KEY = "KNziwqdninINDidwqdji192j9e1cmkasdnaksdnii932niNINi39rnd";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAccount() throws Exception {
        List<Account> expectedOutput = new ArrayList<>();
        Account account1 = Account.builder()
                .username("test")
                .password("test")
                .role("user")
                .name("Test")
                .email("email@test.com")
                .phone("1234567890")
                .build();
            account1.setCartId(account1.getId());
            account1.setHistoryId(account1.getId());

        Account account2 = Account.builder()
                .username("test2")
                .password("test2")
                .role("user")
                .name("Test2")
                .email("test2@gmail.com")
                .phone("1234567890")
                .build();
            account2.setCartId(account2.getId());
            account2.setHistoryId(account2.getId());
        
        expectedOutput.add(account1);
        expectedOutput.add(account2);
        when(accountService.getAllAccount()).thenReturn(expectedOutput);

        mvc.perform(
            get("/account/all")
            .header("X-API-KEY", KEY)
        ).andExpect(
            result -> {
                var response = result.getResponse();
                var responseContent = response.getContentAsString();

                var expectedResponseJson = new ArrayList<>();
                expectedResponseJson.add(account1);
                expectedResponseJson.add(account2);

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    var expectedValueAsString = objectMapper.writeValueAsString(expectedResponseJson);
                    assertEquals(expectedValueAsString, responseContent);
                } catch (Exception e) {
                    e.printStackTrace();
                }   
            }
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk());
    }

    @Test
    void testGetAccountByToken() throws Exception {
        String mockToken = "mockToken";

        Account example = Account.builder()
                .username("test")
                .password("test")
                .role("user")
                .name("Test")
                .email("test@test.com")
                .phone("1234567890")
                .build();
            example.setCartId(example.getId());
            example.setHistoryId(example.getId());
        
        when(accountService.getAccountFromToken(Mockito.any())).thenReturn(example);

        mvc
        .perform(
            get("/account/get-account")
            .header("Authorization", mockToken)
            .header("X-API-KEY", KEY)
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(
            result -> {
                ObjectMapper objectMapper = new ObjectMapper();
                var response = objectMapper.readValue(result.getResponse().getContentAsString(), Account.class);

                assertEquals(example.getId(), response.getId());
                assertEquals(example.getUsername(), response.getUsername());
                assertEquals(example.getRole(), response.getRole());
                assertEquals(example.getName(), response.getName());
                assertEquals(example.getEmail(), response.getEmail());
                assertEquals(example.getPhone(), response.getPhone());
            }
        );
    }

}
