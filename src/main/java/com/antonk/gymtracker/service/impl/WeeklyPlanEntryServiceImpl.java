package com.antonk.gymtracker.service.impl;

import com.antonk.gymtracker.dto.EntryDto;
import com.antonk.gymtracker.entity.WeeklyPlan;
import com.antonk.gymtracker.entity.WeeklyPlanEntry;
import com.antonk.gymtracker.entity.Workout;
import com.antonk.gymtracker.exception.AppException;
import com.antonk.gymtracker.repository.WeeklyPlanEntryRepository;
import com.antonk.gymtracker.repository.WeeklyPlanRepository;
import com.antonk.gymtracker.repository.WorkoutRepository;
import com.antonk.gymtracker.service.WeeklyPlanEntryService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
class WeeklyPlanEntryServiceImpl implements WeeklyPlanEntryService {

    private WeeklyPlanEntryRepository weeklyPlanEntryRepository;
    private WorkoutRepository workoutRepository;
    private WeeklyPlanRepository weeklyPlanRepository;

    @Override
    public List<EntryDto> getWeeklyPlanEntries(UUID weeklyPlanId) {
        return weeklyPlanEntryRepository.findByWeeklyPlan_WeeklyPlanId(weeklyPlanId)
                .stream()
                .map(EntryDto::fromEntity)
                .toList();
    }

    @Override
    public WeeklyPlanEntry addWeeklyPlanEntry(UUID workoutId, UUID weeklyPlanId, DayOfWeek dow) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new AppException("Workout not found",  HttpStatus.NOT_FOUND));

        WeeklyPlan weeklyPlan = weeklyPlanRepository.findById(weeklyPlanId)
                .orElseThrow(() -> new AppException("Weekly plan not found",  HttpStatus.NOT_FOUND));

        WeeklyPlanEntry entry = WeeklyPlanEntry.builder()
                .workout(workout)
                .weeklyPlan(weeklyPlan)
                .dayOfWeek(dow)
                .build();

        return weeklyPlanEntryRepository.save(entry);
    }

    @Override
    public WeeklyPlanEntry updateWeeklyPlanEntry(UUID weeklyPlanEntryId, UUID workoutId){
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new AppException("Workout not found",  HttpStatus.NOT_FOUND));

        WeeklyPlanEntry entry = weeklyPlanEntryRepository.findById(weeklyPlanEntryId)
                .orElseThrow(() -> new AppException("Weekly Plan Entry not found",  HttpStatus.NOT_FOUND));

        entry.setWorkout(workout);

        return weeklyPlanEntryRepository.save(entry);
    }

    @Override
    public void deleteWeeklyPlanEntry(UUID weeklyEntryId) {
        weeklyPlanEntryRepository.deleteById(weeklyEntryId);
    }

    @Override
    @Transactional
    public void deleteAllEntriesByWorkout(UUID workoutId){
        weeklyPlanEntryRepository.deleteAllByWorkout_WorkoutId(workoutId);
    }
}
