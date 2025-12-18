package com.antonk.gymtracker.service;

import com.antonk.gymtracker.dto.CalendarEntryDto;
import com.antonk.gymtracker.dto.CalendarWorkoutDto;
import com.antonk.gymtracker.dto.ScheduledWorkoutUpdateDto;
import com.antonk.gymtracker.entity.ScheduledWorkout;
import com.antonk.gymtracker.entity.enums.WorkoutStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ScheduledWorkoutService {

    List<CalendarEntryDto> getScheduledWorkouts (UUID userId, LocalDate from, LocalDate to);
    ScheduledWorkout generateScheduledWorkout (UUID userId, UUID workoutId, LocalDate date, WorkoutStatus status);
    CalendarWorkoutDto getScheduledWorkout (UUID scheduledWorkoutId);
    void refreshAllStatuses(UUID userId);
    void changeWorkoutStatus (UUID scheduledWorkoutId, WorkoutStatus status);
    void updateScheduledWorkout (UUID scheduledWorkoutId, ScheduledWorkoutUpdateDto scheduledWorkoutUpdateDto);
    void deleteScheduledWorkout (UUID scheduledWorkoutId);
    //void deleteScheduledWorkouts
}
