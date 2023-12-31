package com.organicshop.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRegisterControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetRegisterShowsUp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/register")
                .with(csrf())).andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void testRegistrationWorksCorrectly() throws Exception {
        mockMvc.perform(post("/users/register")
                .param("firstName", "FirstNameTest")
                .param("lastName", "LastNameTest")
                .param("username", "userTest")
                .param("email", "user@user.com")
                .param("password", "password1!")
                .param("confirmPassword", "password1!")
                .param("age", "42")
                .param("phoneNumber", "0888123456")
                .param("gender", "MALE")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));

    }

    @Test
    void testRegistrationWithInvalidData() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("firstName", "")
                        .param("lastName", "LastNameTest")
                        .param("username", "userTest")
                        .param("email", "user@user.com")
                        .param("password", "password1!")
                        .param("confirmPassword", "topsecret")
                        .param("age", "42")
                        .param("phoneNumber", "0888123456")
                        .param("gender", "MALE")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"));

    }


}
