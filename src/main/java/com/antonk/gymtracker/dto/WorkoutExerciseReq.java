package com.antonk.gymtracker.dto;

import java.util.List;
import java.util.UUID;

public record WorkoutExerciseReq(
        UUID exerciseId,
        List<WorkoutSetDto> sets
) {
}
