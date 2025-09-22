package com.antonk.gymtracker.controller;

import com.antonk.gymtracker.dto.*;
import com.antonk.gymtracker.entity.WorkoutExercise;
import com.antonk.gymtracker.service.WorkoutExerciseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/workout_exercise")
@AllArgsConstructor
class WorkoutExerciseController {

    WorkoutExerciseService workoutExerciseService;

    @GetMapping("/{workoutId}/all")
    public ResponseEntity<List<WorkoutExerciseDto>> getWorkoutExercises(@PathVariable UUID workoutId) {
        return ResponseEntity.ok(workoutExerciseService.getWorkoutExercises(workoutId));
    }

    @GetMapping(path= "/find")
    public WorkoutExercise getWorkoutExerciseById(UUID workoutExerciseId){
        return workoutExerciseService.getWorkoutExerciseById(workoutExerciseId);
    }

    @PostMapping(path ="/{workoutId}/create")
    public ResponseEntity<?> createWorkoutExercise(
            @PathVariable UUID workoutId,
            @RequestBody WorkoutExerciseReq req){
        return ResponseEntity.ok(workoutExerciseService.createWorkoutExercise(workoutId, req));
    }

    @PutMapping("/{workoutExerciseId}/update")
    public ResponseEntity<WorkoutExerciseDto> updateWorkoutExercise(
            @PathVariable UUID workoutExerciseId,
            @RequestBody List<WorkoutSetDto> updatedSets
    ) {
        return ResponseEntity.ok(workoutExerciseService.updateWorkoutExercise(workoutExerciseId, updatedSets));
    }



    @PutMapping("/{workoutId}/reorder")
    public ResponseEntity<?> reorderExercises(
            @PathVariable UUID workoutId,
            @RequestBody List<WorkoutExerciseOrderDto> newOrder) {
        workoutExerciseService.updateWorkoutExerciseOrder(workoutId, newOrder);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/superset")
    public void supersetWorkoutExercise(@RequestBody SupersetRequest supersetRequest){
        workoutExerciseService.updateWorkoutExerciseSupersetGroup(supersetRequest.id1(), supersetRequest.id2());
    }

    @PutMapping("/superset/remove")
    public void removeSupersetWorkoutExercise(@RequestBody SupersetRequest supersetRequest){
        workoutExerciseService.removeWorkoutExerciseSupersetGroup(supersetRequest.id1(), supersetRequest.id2());
    }

    @DeleteMapping("/delete/{workoutExerciseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkoutExercise(@PathVariable UUID workoutExerciseId){
        workoutExerciseService.deleteWorkoutExercise(workoutExerciseId);
    }
}
