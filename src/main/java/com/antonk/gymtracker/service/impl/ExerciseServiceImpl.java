package com.antonk.gymtracker.service.impl;

import com.antonk.gymtracker.dto.ExerciseDto;
import com.antonk.gymtracker.entity.Exercise;
import com.antonk.gymtracker.exception.AppException;
import com.antonk.gymtracker.repository.ExerciseRepository;
import com.antonk.gymtracker.service.ExerciseService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private ExerciseRepository exerciseRepository;


    @Override
    public List<Exercise> getUserExercises(UUID exerciseId){
        return exerciseRepository.findByUserId(exerciseId);
    }

    public Exercise getExerciseById(UUID exerciseId){
        return exerciseRepository.findById(exerciseId).orElse(null);
    }

    public List<Exercise> getExercises(){
        return exerciseRepository.findAll();
    }

    @Transactional
    public Exercise createExercise(UUID userId, ExerciseDto exerciseDto){
        Exercise exercise = new Exercise(
                exerciseDto.exerciseName(),
                exerciseDto.exerciseDescription(),
                exerciseDto.muscleGroup(),
                exerciseDto.equipment()
        );
        exercise.setUserId(userId);
        exercise.setPublic(false);
        return exerciseRepository.save(exercise);
    }

    @Transactional
    public Exercise updateExercise(UUID exerciseId, ExerciseDto exerciseDto){
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException("Exercise not found", HttpStatus.NOT_FOUND));

        exercise.setExerciseName(exerciseDto.exerciseName());
        exercise.setExerciseDescription(exerciseDto.exerciseDescription());
        exercise.setMuscleGroup(exerciseDto.muscleGroup());
        exercise.setEquipment(exerciseDto.equipment());

        return exerciseRepository.save(exercise);
    }

    public void deleteAllUserExercises(UUID userId){
        List<Exercise> exercises = exerciseRepository.findByUserId(userId);
        exerciseRepository.deleteAll(exercises);
    }

    public void deleteExercise(UUID exerciseId){
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException("Exercise not found", HttpStatus.NOT_FOUND));
        exerciseRepository.deleteById(exercise.getExerciseId());
    }
}
