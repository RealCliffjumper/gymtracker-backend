package com.antonk.gymtracker.dto;

import com.antonk.gymtracker.entity.enums.MuscleGroup;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateWorkoutDto(
        String workoutName,
        String workoutDescription,
        List<MuscleGroup> muscleGroups,
        LocalDateTime updatedAt
) {
}
