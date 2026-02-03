package com.antonk.gymtracker.repository;

import com.antonk.gymtracker.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {

    List<Exercise> findByUserId(UUID userId);

    List<Exercise> findAllByIsPublicIsTrueOrderByExerciseNameAsc();

    List<Exercise> findByUserIdOrderByExerciseNameAsc(UUID userId);

    @NativeQuery("SELECT * FROM exercises WHERE user_id = :userId ORDER BY exercise_name LIMIT 5 ")
    List<Exercise> findFiveExercisesByUserId(UUID userId);
}
