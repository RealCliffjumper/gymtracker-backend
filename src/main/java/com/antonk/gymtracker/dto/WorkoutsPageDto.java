package com.antonk.gymtracker.dto;

import java.util.UUID;

public record WorkoutsPageDto(String workoutName,
                              UUID workoutId) {
}
