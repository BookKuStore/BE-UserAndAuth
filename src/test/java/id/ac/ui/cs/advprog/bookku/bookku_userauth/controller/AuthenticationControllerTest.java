package id.ac.ui.cs.advprog.bookku.bookku_userauth.controller;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.LoginRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.LoginResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.dto.RegisterResponse;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void testRegister() throws Exception {
        RegisterResponse registerResponse = new RegisterResponse("accessToken", "refreshToken");

        Mockito.when(authenticationService.register(any(RegisterRequest.class))).thenReturn(registerResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"username\", \"password\":\"password\", \"role\":\"role\", \"name\":\"name\", \"email\":\"email\", \"phone\":\"phone\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"accessToken\":\"accessToken\", \"refreshToken\":\"refreshToken\"}"));
    }

    @Test
    void testLogin() throws Exception {
        LoginResponse loginResponse = new LoginResponse("accessToken", "refreshToken");

        Mockito.when(authenticationService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"username\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"accessToken\":\"accessToken\", \"refreshToken\":\"refreshToken\"}"));
    }
    
}
