package com.antonk.gymtracker.service.impl;

import com.antonk.gymtracker.dto.WorkoutExerciseDto;
import com.antonk.gymtracker.dto.WorkoutExerciseOrderDto;
import com.antonk.gymtracker.dto.WorkoutExerciseReq;
import com.antonk.gymtracker.dto.WorkoutSetDto;
import com.antonk.gymtracker.entity.Exercise;
import com.antonk.gymtracker.entity.Workout;
import com.antonk.gymtracker.entity.WorkoutExercise;
import com.antonk.gymtracker.entity.WorkoutSet;
import com.antonk.gymtracker.exception.AppException;
import com.antonk.gymtracker.repository.ExerciseRepository;
import com.antonk.gymtracker.repository.WorkoutExerciseRepository;
import com.antonk.gymtracker.repository.WorkoutRepository;
import com.antonk.gymtracker.service.WorkoutExerciseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {

    private final WorkoutExerciseRepository workoutExerciseRepository;

    private final ExerciseRepository exerciseRepository;

    private final WorkoutRepository workoutRepository;

    private WorkoutExerciseDto mapToDto(WorkoutExercise we) {
        String exerciseName = exerciseRepository.findById(we.getExerciseId())
                .map(Exercise::getExerciseName)
                .orElse("Unknown Exercise");


        List<WorkoutSetDto> sets = we.getSets().stream()
                .map(s -> new WorkoutSetDto(
                        s.getSetNumber(),
                        s.getReps(),
                        s.getWeight()))
                .toList();

        return new WorkoutExerciseDto(
                we.getWorkoutExerciseId(),
                we.getExerciseId(),
                we.getExerciseOrder(),
                exerciseName,
                sets
        );
    }

    public List<WorkoutExerciseDto> getWorkoutExercises(UUID workoutId) {
        List<WorkoutExercise> workoutExercises = workoutExerciseRepository.findByWorkoutIdOrderByExerciseOrderAsc(workoutId); //sorted by order list
        return workoutExercises.stream()
                .map(this::mapToDto)
                .toList();
    }

    public WorkoutExercise getWorkoutExerciseById(UUID workoutExerciseId){
        return  workoutExerciseRepository.findById(workoutExerciseId).
                orElseThrow(() -> new AppException("Workout exercise not found", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public WorkoutExercise createWorkoutExercise(UUID workoutId, WorkoutExerciseReq req){
        WorkoutExercise lastWorkoutExercise = workoutExerciseRepository.findFirstByWorkoutIdOrderByExerciseOrderDesc(workoutId);

        WorkoutExercise workoutExercise = WorkoutExercise.builder()
                .exerciseId(req.exerciseId())
                .workoutId(workoutId)
                .exerciseOrder(lastWorkoutExercise != null ? lastWorkoutExercise.getExerciseOrder() + 1 : 0)
                .build();


        List<WorkoutSet> sets = req.sets().stream()
                .map(dto -> WorkoutSet.builder()
                        .setNumber(dto.setNumber())
                        .reps(dto.reps())
                        .weight(dto.weight())
                        .workoutExercise(workoutExercise)
                        .build())
                .toList();

        workoutExercise.setSets(sets);

        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new AppException("Workout not found", HttpStatus.NOT_FOUND));
        workout.setUpdatedAt(LocalDateTime.now());
        workoutRepository.save(workout);

        workoutExercise.setWorkoutId(workoutId);
        workoutExercise.setExerciseId(req.exerciseId());
        return workoutExerciseRepository.save(workoutExercise);
    }

    @Transactional
    public WorkoutExerciseDto updateWorkoutExercise(UUID workoutExerciseId, List<WorkoutSetDto> updatedSets) {
        WorkoutExercise workoutExercise = workoutExerciseRepository.findById(workoutExerciseId)
                .orElseThrow(() -> new AppException("Workout exercise not found", HttpStatus.NOT_FOUND));

        workoutExercise.getSets().clear();

        List<WorkoutSet> sets = updatedSets.stream()
                .map(dto -> WorkoutSet.builder()
                        .workoutExercise(workoutExercise) // important: set back-reference
                        .setNumber(dto.setNumber())
                        .reps(dto.reps())
                        .weight(dto.weight())
                        .build())
                .toList();

        workoutExercise.getSets().addAll(sets);

        workoutExerciseRepository.save(workoutExercise);

        return mapToDto(workoutExercise);
    }


    public void updateWorkoutExerciseOrder(UUID workoutId, List<WorkoutExerciseOrderDto> dto){
        List<WorkoutExercise> exercises = workoutExerciseRepository.findByWorkoutIdOrderByExerciseOrderAsc(workoutId);

        Map<UUID, Integer> newOrderMap = dto.stream()
                .collect(Collectors.toMap(WorkoutExerciseOrderDto::workoutExerciseId,
                        WorkoutExerciseOrderDto::exerciseOrder));

        for (WorkoutExercise exercise : exercises) {
            Integer newPos = newOrderMap.get(exercise.getWorkoutExerciseId());
            if (newPos != null && exercise.getExerciseOrder() != newPos) {
                exercise.setExerciseOrder(newPos);
            }
        }

        workoutExerciseRepository.saveAll(exercises);
    }

    public void updateWorkoutExerciseSupersetGroup(UUID workoutExerciseId1, UUID workoutExerciseId2){
        WorkoutExercise workoutExercise1 = workoutExerciseRepository.findById(workoutExerciseId1).
                orElseThrow(() -> new AppException("Workout not found", HttpStatus.NOT_FOUND));

        WorkoutExercise workoutExercise2 = workoutExerciseRepository.findById(workoutExerciseId2).
                orElseThrow(() -> new AppException("Workout not found", HttpStatus.NOT_FOUND));

        UUID supersetGroupId = UUID.randomUUID();

        workoutExercise1.setSupersetGroupId(supersetGroupId);
        workoutExercise2.setSupersetGroupId(supersetGroupId);

        workoutExerciseRepository.save(workoutExercise1);
        workoutExerciseRepository.save(workoutExercise2);
    }

    public void deleteAllWorkoutExercises(UUID workoutId){
        List<WorkoutExercise> workoutExercises = workoutExerciseRepository.findByWorkoutId(workoutId);
        workoutExerciseRepository.deleteAll(workoutExercises);
    }

    public void deleteWorkoutExercise(UUID workoutExerciseId){
        WorkoutExercise workoutExercise = workoutExerciseRepository.findById(workoutExerciseId).
                orElseThrow(() -> new AppException("Workout exercise not found", HttpStatus.NOT_FOUND));

        workoutExerciseRepository.deleteById(workoutExercise.getWorkoutExerciseId());
    }
}
