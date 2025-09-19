package com.antonk.gymtracker.dto;

import java.util.UUID;

public record WorkoutExerciseOrderDto(UUID workoutExerciseId, int exerciseOrder) {
}
