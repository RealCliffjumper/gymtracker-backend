package com.antonk.gymtracker.service;

import com.antonk.gymtracker.dto.UpdateWorkoutDto;
import com.antonk.gymtracker.dto.WorkoutDto;
import com.antonk.gymtracker.dto.WorkoutsPageDto;
import com.antonk.gymtracker.entity.Workout;

import java.util.List;
import java.util.UUID;

public interface WorkoutService {

    List<WorkoutsPageDto> getUserWorkouts(UUID userId);

    Workout getWorkoutById(UUID workoutId);

    Workout createWorkout(UUID userId, WorkoutDto workoutDto);

    Workout updateWorkout(UUID workoutId, UpdateWorkoutDto updateWorkoutDto);

    List<String> findAllPlanNames(UUID workoutId);

    void deleteAllUserWorkouts(UUID userId);

    void deleteWorkout(UUID workoutId);
}
