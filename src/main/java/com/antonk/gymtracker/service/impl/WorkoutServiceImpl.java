package com.antonk.gymtracker.service.impl;

import com.antonk.gymtracker.dto.UpdateWorkoutDto;
import com.antonk.gymtracker.dto.WorkoutDto;
import com.antonk.gymtracker.entity.User;
import com.antonk.gymtracker.entity.Workout;
import com.antonk.gymtracker.exception.AppException;
import com.antonk.gymtracker.repository.WorkoutRepository;
import com.antonk.gymtracker.service.WorkoutService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private WorkoutRepository workoutRepository;

    @Override
    public List<Workout> getUserWorkouts(UUID userId) {
        return workoutRepository.findByUserId(userId);
    }

    public Workout getWorkoutById(UUID workoutId) {return workoutRepository.findById(workoutId).orElse(null);}

    @Transactional
    @Override
    public Workout createWorkout(UUID userId, WorkoutDto workoutDto) {
        Workout workout = new Workout(
                workoutDto.workoutName(),
                workoutDto.workoutDescription(),
                workoutDto.createdAt()
        );
        workout.setUserId(userId);
        workout.setUpdatedAt(LocalDateTime.now());
        return workoutRepository.save(workout);
    }

    @Transactional
    @Override
    public Workout updateWorkout(UUID workoutId, UpdateWorkoutDto updateWorkoutDto) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new AppException("Workout not found", HttpStatus.NOT_FOUND));

        boolean noChanges = Objects.equals(workout.getWorkoutName(), updateWorkoutDto.workoutName()) &&
                Objects.equals(workout.getWorkoutDescription(), updateWorkoutDto.workoutDescription());

        workout.setWorkoutName(updateWorkoutDto.workoutName());
        workout.setWorkoutDescription(updateWorkoutDto.workoutDescription());
        workout.setUpdatedAt(LocalDateTime.now());

        if (noChanges) {
            throw new AppException("No changes were made", HttpStatus.NOT_MODIFIED);
        }

        return workoutRepository.save(workout);
    }

    @Override
    public void deleteAllUserWorkouts(UUID userId) {
        List<Workout> workouts = workoutRepository.findByUserId(userId);
        workoutRepository.deleteAll(workouts);
    }

    @Override
    public void deleteWorkout(UUID workoutId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new AppException("Workout not found", HttpStatus.NOT_FOUND));
        workoutRepository.deleteById(workout.getWorkoutId());
    }
}
