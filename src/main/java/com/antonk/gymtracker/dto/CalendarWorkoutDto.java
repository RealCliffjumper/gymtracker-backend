package com.antonk.gymtracker.dto;

import com.antonk.gymtracker.entity.ScheduledWorkoutExercise;
import com.antonk.gymtracker.entity.enums.MuscleGroup;
import com.antonk.gymtracker.entity.enums.WorkoutStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record CalendarWorkoutDto(
        UUID scheduledWorkoutId,  // null if virtual
        UUID workoutId,
        String workoutName,
        String workoutDescription,
        LocalDate workoutScheduledDate,
        List<MuscleGroup> muscleGroups,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        double workoutPoints,
        List<ScheduledWorkoutExercise> exercises,
        boolean isVirtual,       // true if just-in-time from plan
        WorkoutStatus status) {
}
