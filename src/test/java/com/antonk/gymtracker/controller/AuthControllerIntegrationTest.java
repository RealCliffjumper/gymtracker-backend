package com.antonk.gymtracker.controller;

import com.antonk.gymtracker.dto.LoginDto;
import com.antonk.gymtracker.dto.SignUpDto;
import com.antonk.gymtracker.entity.User;
import com.antonk.gymtracker.entity.enums.UnitPreference;
import com.antonk.gymtracker.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;



    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void loginUser_success_returnsJwtDto() throws Exception {

        LoginDto loginDto = new LoginDto("test@example.com", "password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists()) // JWT returned
                .andExpect(jsonPath("$.user.userLoginId").value("test@example.com"));
    }

    @Test
    void loginUser_userNotFound_returns404() throws Exception {
        LoginDto loginDto = new LoginDto("unknown@example.com", "password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void loginUser_invalidPassword_returns400() throws Exception {
        LoginDto loginDto = new LoginDto("test@example.com", "wrongpassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid password"));
    }

    @Test
    void signUpUser_success_returnsJwtDto() throws Exception {
        SignUpDto signUpDto = new SignUpDto("test@example.com", "Test", "Test", "test", null, UnitPreference.KG);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/registration")
                .content(objectMapper.writeValueAsString(signUpDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists()) // JWT returned
                .andExpect(jsonPath("$.user.userLoginId").value("test@example.com"));

        User user = userRepository.findByUserLoginId("test@example.com").orElseThrow();
        var userId = user.getUserId();

        userRepository.deleteById(userId);
    }

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void signUpUser_loginIdConflict_returnsException() throws Exception{
        SignUpDto signUpDto = new SignUpDto("test@example.com", "Test", "Test", "test", null, UnitPreference.KG);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/registration")
                        .content(objectMapper.writeValueAsString(signUpDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Username already exists"));
    }

    //probably add some regex tests too after actual regex implementation
}