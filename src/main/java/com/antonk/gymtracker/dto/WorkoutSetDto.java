package com.antonk.gymtracker.dto;

import java.util.UUID;

public record WorkoutSetDto(
        UUID setLogId,
        int setNumber,
        int reps,
        int weight) {
}
