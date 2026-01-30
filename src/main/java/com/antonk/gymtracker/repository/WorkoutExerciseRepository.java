package com.antonk.gymtracker.repository;

import com.antonk.gymtracker.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, UUID> {

    List<WorkoutExercise> findByWorkout_WorkoutIdOrderByExerciseOrderAsc(UUID workoutId);

    WorkoutExercise findFirstByWorkout_WorkoutIdOrderByExerciseOrderDesc(UUID workoutId);

    List<WorkoutExercise> findByWorkout_WorkoutId(UUID workoutId);
}
