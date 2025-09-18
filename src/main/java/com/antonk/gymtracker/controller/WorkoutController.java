package com.antonk.gymtracker.controller;

import com.antonk.gymtracker.dto.UpdateUserDto;
import com.antonk.gymtracker.dto.UpdateWorkoutDto;
import com.antonk.gymtracker.dto.WorkoutDto;
import com.antonk.gymtracker.entity.User;
import com.antonk.gymtracker.entity.Workout;
import com.antonk.gymtracker.service.WorkoutService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/workout")
@AllArgsConstructor
class WorkoutController {

    private WorkoutService workoutService;

    @PostMapping(path = "{userId}/create")
    public ResponseEntity<?> createWorkout(@PathVariable UUID userId, @RequestBody WorkoutDto workoutDto) {
        Workout workout = workoutService.createWorkout(userId, workoutDto);
        return ResponseEntity.ok(workout);
    }

    @GetMapping(path = "{userId}/all")
    public List<Workout> getWorkouts(@PathVariable UUID userId) {
        return workoutService.getUserWorkouts(userId);
    }

    @GetMapping(path = "{workoutId}/find")
    public Workout getWorkout(@PathVariable UUID workoutId) {
        return workoutService.getWorkoutById(workoutId);
    }

    @PutMapping("{workoutId}/update")
    public ResponseEntity<Workout> updateWorkout(
            @PathVariable UUID workoutId,
            @RequestBody UpdateWorkoutDto dto
    ) {
        Workout updated = workoutService.updateWorkout(workoutId, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "delete/{workoutId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  deleteWorkout(@PathVariable UUID workoutId) {
        workoutService.deleteWorkout(workoutId);
    }
}
