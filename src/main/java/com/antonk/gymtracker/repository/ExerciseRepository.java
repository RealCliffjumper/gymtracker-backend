package com.antonk.gymtracker.repository;

import com.antonk.gymtracker.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {

    List<Exercise> findByUserId(UUID userId);
}
