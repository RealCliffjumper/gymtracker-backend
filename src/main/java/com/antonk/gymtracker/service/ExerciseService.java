package com.antonk.gymtracker.service;


import com.antonk.gymtracker.dto.ExerciseDto;
import com.antonk.gymtracker.entity.Exercise;

import java.util.List;
import java.util.UUID;

public interface ExerciseService {

    List<Exercise> getUserExercises(UUID userId);

    List<Exercise> getExercises();

    List<Exercise> getFiveExercises(UUID userId);

    Exercise getExerciseById(UUID exerciseId);

    Exercise createExercise(UUID userId, ExerciseDto exerciseDto);

    Exercise updateExercise(UUID exerciseId, ExerciseDto exerciseDto);

    void deleteAllUserExercises(UUID userId);

    void deleteExercise(UUID exerciseId);
}
