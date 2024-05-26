package id.ac.ui.cs.advprog.bookku.bookku_userauth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.AuthorizeRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.AuthorizeResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RefreshRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RefreshResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.service.AuthorizationService;

@WebMvcTest(controllers = AuthorizationController.class)
@AutoConfigureMockMvc
class AuthorizationControllerTests {

    private String KEY = "KNziwqdninINDidwqdji192j9e1cmkasdnaksdnii932niNINi39rnd";

    private String mockAccessToken = "awiflaoiwfjaiwfja";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorizationService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void authorizeSuccessTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", mockAccessToken);
        headers.add("X-API-KEY", KEY);


        var authorizeRequest = AuthorizeRequest.builder()
                .role("USER")
                .build();

        var authorizeResponse = AuthorizeResponse.builder()
                        .authorized(true)
                        .message("Access granted")
                        .build();

        when(service.authorize(Mockito.any(), Mockito.any())).thenReturn(authorizeResponse);

        mvc
            .perform(
                    post("/authorization/authorize")
                            .content(objectMapper.writeValueAsString(authorizeRequest))
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(result -> {
                var response = objectMapper.readValue(result.getResponse().getContentAsString(), AuthorizeResponse.class);
                Assertions.assertEquals(authorizeResponse.getMessage(), response.getMessage());
            });
    }

    @Test
    void refreshingTokenTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        RefreshRequest refreshingTokenRequest = RefreshRequest.builder()
                .refreshToken("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiYWRtaW4iLCJ1c2VybmFtZSI6ImxrYWx3a2psYWt3amRsYXdkIiwic3ViIjoiMSIsImlhdCI6MTY4MTc0OTgxOSwiZXhwIjoxNjgxNzkzMDE5fQ.8dM_x-RqADhsvt-zDTj4J92W52mbmMq5bgApS_IXi0I")
                .build();

        RefreshResponse refreshingTokenResponse = RefreshResponse.builder()
                .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoicHJvZ3JhbW1hZXIiLCJ1c2VybmFtZSI6ImRvaSIsInN1YiI6IjEiLCJpYXQiOjE2ODE3Mjk0ODIsImV4cCI6MTY4MTczMTI4Mn0.A0V_ALzEbFq-DeiiAYhhDAY-DB10iyhV58GeFwFrRVw")
                .build();

        when(service.refresh(refreshingTokenRequest)).thenReturn(refreshingTokenResponse);

        mvc
            .perform(
                    post("/authorization/refresh").header("X-API-KEY", KEY)
                            .content(objectMapper.writeValueAsString(refreshingTokenRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(result -> {
                var response = objectMapper.readValue(result.getResponse().getContentAsString(), RefreshResponse.class);
                Assertions.assertEquals(refreshingTokenResponse.getAccessToken(), response.getAccessToken());
            });
    }


}
