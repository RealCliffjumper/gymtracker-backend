package com.antonk.gymtracker.controller;

import com.antonk.gymtracker.dto.ExerciseDto;
import com.antonk.gymtracker.entity.User;
import com.antonk.gymtracker.entity.enums.Equipment;
import com.antonk.gymtracker.entity.enums.MuscleGroup;
import com.antonk.gymtracker.repository.ExerciseRepository;
import com.antonk.gymtracker.repository.UserRepository;
import com.antonk.gymtracker.service.ExerciseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExerciseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ExerciseService exerciseService;


    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-exercise.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createExercise_shouldReturn_newExercise() throws Exception {
        User user = userRepository.findByUserLoginId("test@example.com").orElseThrow();
        UUID userId = user.getUserId();

        ExerciseDto exerciseDto = new ExerciseDto("Test", "a test exercise", MuscleGroup.UPPER_BACK, Equipment.BARBELL);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/exercise/" + userId + "/create")
                        .content(objectMapper.writeValueAsString(exerciseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exerciseName").value("Test"))
                .andExpect(jsonPath("$.exerciseDescription").value("a test exercise"))
                .andExpect(jsonPath("$.muscleGroup").value("UPPER_BACK"))
                .andExpect(jsonPath("$.equipment").value("BARBELL"));
    }

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/insert-exercises.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/delete-exercises.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value =  "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllExercises_returnsExercises() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/exercise/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].exerciseName", not(emptyString())));
    }

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/insert-exercises.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/delete-exercises.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value =  "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUserExercises_returnsOnlyForUser() throws Exception {
        User user = userRepository.findByUserLoginId("test@example.com").orElseThrow();
        UUID userId = user.getUserId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/exercise/{userId}/all", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].userId", everyItem(is(userId.toString()))));
    }

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/insert-exercises.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/delete-exercises.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value =  "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getExerciseById_returnsExercise() throws Exception {
        UUID exerciseId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/exercise/{exerciseId}/find", exerciseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exerciseId", is(exerciseId.toString())))
                .andExpect(jsonPath("$.exerciseName", not(emptyString())));
    }

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/insert-exercises.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/delete-exercises.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value =  "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateExercise_updatesSuccessfully() throws Exception {
        UUID exerciseId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        ExerciseDto dto = new ExerciseDto(
                "updatedintTest1", "Updated description", MuscleGroup.CHEST, Equipment.DUMBBELL);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/exercise/{exerciseId}/update", exerciseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exerciseName", is("updatedintTest1")))
                .andExpect(jsonPath("$.muscleGroup", is("CHEST")));
    }

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/insert-exercises.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/delete-exercises.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value =  "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteExercise_deletesSuccessfully() throws Exception {
        UUID exerciseId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/exercise/delete/{exerciseId}", exerciseId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/insert-exercises.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/delete-exercises.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value =  "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteAllExercisesForUser_deletesSuccessfully() throws Exception {
        User user = userRepository.findByUserLoginId("test@example.com").orElseThrow();
        UUID userId = user.getUserId();

        exerciseService.deleteAllUserExercises(userId);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/exercise/{userId}/all", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}