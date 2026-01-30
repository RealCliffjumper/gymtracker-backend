package com.antonk.gymtracker.repository;

import com.antonk.gymtracker.dto.WorkoutsPageDto;
import com.antonk.gymtracker.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface WorkoutRepository extends JpaRepository<Workout, UUID> {

    List<Workout> findByUserId(UUID userId);

    @NativeQuery("SELECT DISTINCT weekly_plan_name FROM weekly_plans " +
                "JOIN weekly_plan_entries ON weekly_plans.weekly_plan_id=weekly_plan_entries.weekly_plan_id WHERE workout_id = :workoutId")
    List<String> findAllRelatedPlans(@Param("workoutId")  UUID workoutId);

    @NativeQuery("SELECT workout_name, workout_id FROM workouts WHERE user_id = :userId")
    List<WorkoutsPageDto> findWorkoutsByUserId(UUID userId);
}
