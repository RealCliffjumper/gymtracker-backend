package com.antonk.gymtracker.controller;

import com.antonk.gymtracker.dto.PasswordChangeDto;
import com.antonk.gymtracker.dto.UpdateUserDto;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateUser_success_returnsUpdatedUser() throws Exception {
        User user = userRepository.findByUserLoginId("test@example.com").orElseThrow();
        UUID userId = user.getUserId();

        UpdateUserDto updateDto = new UpdateUserDto("updatetest@example.com", "NewFirstTest", "NewLastTest", UnitPreference.LBS);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/" + userId)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userLoginId").value("updatetest@example.com"))
                .andExpect(jsonPath("$.userFirstName").value("NewFirstTest"))
                .andExpect(jsonPath("$.userLastName").value("NewLastTest"))
                .andExpect(jsonPath("$.unitPreference").value("LBS"));
    }

    @Test
    void updateUser_userNotFound_returns404() throws Exception {
        UUID nonExistentId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UpdateUserDto updateDto = new UpdateUserDto("updatetest@example.com", "NewFirstTest", "NewLastTest", null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/" + nonExistentId)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateUser_partialUpdate_unitPreferenceNull_keepsOldValue() throws Exception {
        User user = userRepository.findByUserLoginId("test@example.com").orElseThrow();
        UUID userId = user.getUserId();

        UpdateUserDto updateDto = new UpdateUserDto("updatetest@example.com", "PartialFirstTest", "PartialLastTest", null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/" + userId)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userFirstName").value("PartialFirstTest"))
                .andExpect(jsonPath("$.unitPreference").value("KG"));
    }

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void changePassword_success_returnsTrueOnEncoderMatch() throws Exception {

        User user = userRepository.findByUserLoginId("test@example.com").orElseThrow();
        UUID userId = user.getUserId();

        String oldPassword = "password123";
        user.setPassword(bCryptPasswordEncoder.encode(oldPassword));
        userRepository.save(user);

        PasswordChangeDto passwordChangeDto = new PasswordChangeDto(oldPassword, "newPassword");
        String newPassword = bCryptPasswordEncoder.encode(passwordChangeDto.newPassword());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/" + userId + "/password")
                .content(objectMapper.writeValueAsString(passwordChangeDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertTrue(bCryptPasswordEncoder.matches(passwordChangeDto.newPassword(), newPassword));
    }

    @Test
    void changePassword_userNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        PasswordChangeDto dto = new PasswordChangeDto("anyPassword", "newPassword");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/" + nonExistentId + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void changePassword_oldPasswordIncorrect() throws Exception {
        User user = userRepository.findByUserLoginId("test@example.com").orElseThrow();
        UUID userId = user.getUserId();

        String oldPassword = "password123";
        user.setPassword(bCryptPasswordEncoder.encode(oldPassword));
        userRepository.save(user);

        PasswordChangeDto dto = new PasswordChangeDto("wrongOldPassword", "newPassword123");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/" + userId + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Old password is incorrect"));
    }

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteUser_success_returnsDeleted() throws Exception {
        User user = userRepository.findByUserLoginId("test@example.com").orElseThrow();
        UUID userId = user.getUserId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete/" + userId))
                .andExpect(status().isNoContent());
        assertFalse(userRepository.findById(userId).isPresent());
    }
}