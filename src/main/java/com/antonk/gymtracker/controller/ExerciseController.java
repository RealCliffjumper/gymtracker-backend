package com.antonk.gymtracker.controller;

import com.antonk.gymtracker.dto.ExerciseDto;
import com.antonk.gymtracker.entity.Exercise;
import com.antonk.gymtracker.service.ExerciseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/exercise")

class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping(path = "{userId}/create")
    public ResponseEntity<?> createExercises(@PathVariable UUID userId, @RequestBody ExerciseDto exerciseDto) {
        Exercise exercise = exerciseService.createExercise(userId, exerciseDto);
        return ResponseEntity.ok(exercise);
    }

    @GetMapping("/all")
    public List<Exercise> getAllExercises() {
        return exerciseService.getExercises();
    }
        
    @GetMapping(path = "{userId}/all")
    public List<Exercise> getExercises(@PathVariable UUID userId) {
        return exerciseService.getUserExercises(userId);
    }

    @GetMapping(path = "{userId}/five")
    public List<Exercise> getFiveExercises(@PathVariable UUID userId) {
        return exerciseService.getFiveExercises(userId);
    }

    @GetMapping(path = "{exerciseId}/find")
    public Exercise getExercise(@PathVariable UUID exerciseId) {
        return exerciseService.getExerciseById(exerciseId);
    }

    @PutMapping("{exerciseId}/update")
    public ResponseEntity<Exercise> updateExercise(
            @PathVariable UUID exerciseId,
            @RequestBody ExerciseDto dto
    ) {
        Exercise updated = exerciseService.updateExercise(exerciseId, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "delete/{exerciseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  deleteExercise(@PathVariable UUID exerciseId) {
        exerciseService.deleteExercise(exerciseId);
    }
}
