package com.antonk.gymtracker.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkoutDto(
        String workoutName,
        String workoutDescription,
        LocalDateTime createdAt) {
}
