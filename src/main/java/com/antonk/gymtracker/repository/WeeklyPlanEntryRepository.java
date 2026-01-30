package com.antonk.gymtracker.repository;

import com.antonk.gymtracker.entity.WeeklyPlanEntry;
import com.antonk.gymtracker.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WeeklyPlanEntryRepository extends JpaRepository<WeeklyPlanEntry, UUID> {

    List<WeeklyPlanEntry> findByWeeklyPlan_WeeklyPlanId(UUID weeklyPlanId);

    List<WeeklyPlanEntry> workout(Workout workout);

    void deleteAllByWorkout_WorkoutId(UUID workoutId);
}
