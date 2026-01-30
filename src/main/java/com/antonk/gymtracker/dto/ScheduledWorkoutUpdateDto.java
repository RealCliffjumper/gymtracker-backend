package com.antonk.gymtracker.dto;

import com.antonk.gymtracker.entity.enums.MuscleGroup;
import com.antonk.gymtracker.entity.enums.WorkoutStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ScheduledWorkoutUpdateDto(
        String workoutNotes,
        WorkoutStatus status,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        List<MuscleGroup> muscleGroups,
        List<ScheduledExerciseUpdateDto> exercises,
        int workoutPoints
) {
}
