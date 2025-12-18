package com.antonk.gymtracker.dto;

import com.antonk.gymtracker.entity.enums.WorkoutStatus;

import java.time.LocalDate;
import java.util.UUID;

public record CalendarWorkoutReq(
        UUID workoutId,
        LocalDate workoutScheduledDate,
        WorkoutStatus workoutStatus

) {
}
