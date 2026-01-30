package com.antonk.gymtracker.dto;

import com.antonk.gymtracker.entity.WeeklyPlanEntry;

import java.time.DayOfWeek;
import java.util.UUID;

public record EntryDto(UUID weeklyEntryId,
                       DayOfWeek dayOfWeek,
                       UUID workoutId,
                       String workoutName)
{
    public static EntryDto fromEntity(WeeklyPlanEntry entry) {
        return new EntryDto(
                entry.getWeeklyEntryId(),
                entry.getDayOfWeek(),
                entry.getWorkout().getWorkoutId(),
                entry.getWorkout().getWorkoutName()
        );
    }
}
