package com.antonk.gymtracker.service;

import com.antonk.gymtracker.dto.EntryDto;
import com.antonk.gymtracker.entity.WeeklyPlanEntry;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public interface WeeklyPlanEntryService {

    List<EntryDto> getWeeklyPlanEntries(UUID weeklyPlanId);

    WeeklyPlanEntry getWeeklyPlanEntry(UUID weeklyPlanEntryId);

    WeeklyPlanEntry updateWeeklyPlanEntry(UUID weeklyPlanEntryId, UUID workoutId);

    WeeklyPlanEntry addWeeklyPlanEntry(UUID workoutId, UUID weeklyPlanId, DayOfWeek dayOfWeek);

    void deleteWeeklyPlanEntry(UUID weeklyEntryId);
}
