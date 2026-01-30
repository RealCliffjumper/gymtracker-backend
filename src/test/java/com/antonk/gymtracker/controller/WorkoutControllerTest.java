package com.antonk.gymtracker.controller;

import com.antonk.gymtracker.dto.UpdateUserDto;
import com.antonk.gymtracker.dto.UpdateWorkoutDto;
import com.antonk.gymtracker.dto.WorkoutDto;
import com.antonk.gymtracker.entity.User;
import com.antonk.gymtracker.entity.Workout;
import com.antonk.gymtracker.entity.enums.UnitPreference;
import com.antonk.gymtracker.repository.UserRepository;
import com.antonk.gymtracker.repository.WorkoutRepository;
import com.antonk.gymtracker.service.WorkoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WorkoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private WorkoutService workoutService;

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-workout.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createWorkout_shouldReturn_newWorkout() throws Exception {
        User user = userRepository.findByUserLoginId("test@example.com").orElseThrow();
        UUID userId = user.getUserId();

        WorkoutDto workoutDto = new WorkoutDto("testWorkout","a test workout",null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/workout/" + userId + "/create")
                        .content(objectMapper.writeValueAsString(workoutDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workoutName").value("testWorkout"))
                .andExpect(jsonPath("$.workoutDescription").value("a test workout"));
    }

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-workout.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUserWorkouts_whenWorkoutsExist_returnsList() throws Exception {
        User user = userRepository.findByUserLoginId("test@example.com").orElseThrow();
        UUID userId = user.getUserId();

        Workout workout1 = new Workout();
        workout1.setUserId(userId);
        workout1.setWorkoutName("testWorkout1");
        workout1.setWorkoutDescription("test workout 1");

        Workout workout2 = new Workout();
        workout2.setUserId(userId);
        workout2.setWorkoutName("testWorkout2");
        workout2.setWorkoutDescription("test workout 2");

        workoutRepository.saveAll(List.of(workout1, workout2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/workout/" + userId + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].workoutName").value("testWorkout1"))
                .andExpect(jsonPath("$[1].workoutName").value("testWorkout2"));
    }

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/insert-workout.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteWorkout_success_returnsDeleted() throws Exception {
        User user = userRepository.findByUserLoginId("test@example.com").orElseThrow();
        UUID userId = user.getUserId();

        Workout workout = workoutService.getUserWorkouts(userId).stream().findFirst().get();
        UUID workoutId = workout.getWorkoutId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/workout/"+ workoutId +"/delete"))
                .andExpect(status().isNoContent());
        assertFalse(userRepository.findById(workoutId).isPresent());
    }

    @Test
    @Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/insert-workout.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-workout.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateWorkout_shouldReturn_updatedWorkout() throws Exception {
        User user = userRepository.findByUserLoginId("test@example.com").orElseThrow();
        UUID userId = user.getUserId();

        Workout workout = workoutService.getUserWorkouts(userId).stream().findFirst().get();
        UUID workoutId = workout.getWorkoutId();

        UpdateWorkoutDto updateDto = new UpdateWorkoutDto("updatedWorkout","an updated workout",null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/workout/" + workoutId + "/update")
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workoutName").value("updatedWorkout"))
                .andExpect(jsonPath("$.workoutDescription").value("an updated workout"));
    }
}