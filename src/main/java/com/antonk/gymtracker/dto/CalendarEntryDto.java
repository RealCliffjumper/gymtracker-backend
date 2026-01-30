package com.antonk.gymtracker.dto;

import com.antonk.gymtracker.entity.enums.WorkoutStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record CalendarEntryDto(
        UUID scheduledWorkoutId,
        UUID workoutId,
        String workoutName,
        LocalDate workoutScheduledDate,
        boolean isVirtual,
        WorkoutStatus status
) {
}
