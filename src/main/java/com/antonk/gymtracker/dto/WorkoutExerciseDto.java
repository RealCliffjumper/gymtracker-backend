package com.antonk.gymtracker.dto;

import com.antonk.gymtracker.entity.WorkoutExercise;

import java.util.List;
import java.util.UUID;

public record WorkoutExerciseDto(UUID workoutExerciseId,
                                 UUID exerciseId,
                                 int exerciseOrder,
                                 String exerciseName,
                                 List<WorkoutSetDto> sets) {
}
