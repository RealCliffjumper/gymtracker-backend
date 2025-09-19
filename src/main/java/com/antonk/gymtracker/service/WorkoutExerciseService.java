package com.antonk.gymtracker.service;

import com.antonk.gymtracker.dto.WorkoutExerciseDto;
import com.antonk.gymtracker.dto.WorkoutExerciseOrderDto;
import com.antonk.gymtracker.dto.WorkoutExerciseReq;
import com.antonk.gymtracker.dto.WorkoutSetDto;
import com.antonk.gymtracker.entity.WorkoutExercise;

import java.util.List;
import java.util.UUID;

public interface WorkoutExerciseService {

    List<WorkoutExerciseDto> getWorkoutExercises(UUID workoutId);

    WorkoutExercise getWorkoutExerciseById(UUID workoutExerciseId);

    WorkoutExercise createWorkoutExercise(UUID workoutId, WorkoutExerciseReq req);

    WorkoutExerciseDto updateWorkoutExercise(UUID workoutExerciseId, List<WorkoutSetDto> updatedSets);

    void updateWorkoutExerciseOrder(UUID workoutExerciseId, List<WorkoutExerciseOrderDto> exerciseOrderDto);

    void updateWorkoutExerciseSupersetGroup(UUID workoutExerciseId1, UUID workoutExerciseId2);

    void deleteAllWorkoutExercises(UUID workoutId);

    void deleteWorkoutExercise(UUID workoutExerciseId);
}
