package com.antonk.gymtracker.dto;

import java.time.LocalDateTime;

public record UpdateWorkoutDto(
        String workoutName,
        String workoutDescription,
        LocalDateTime updatedAt
) {
}
