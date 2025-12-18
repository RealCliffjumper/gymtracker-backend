package com.antonk.gymtracker.service.impl;

import com.antonk.gymtracker.dto.*;
import com.antonk.gymtracker.entity.*;
import com.antonk.gymtracker.entity.enums.WorkoutStatus;
import com.antonk.gymtracker.exception.AppException;
import com.antonk.gymtracker.repository.*;
import com.antonk.gymtracker.service.ScheduledWorkoutService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class ScheduledWorkoutServiceImpl implements ScheduledWorkoutService {

    private final WorkoutRepository workoutRepository;
    private WeeklyPlanRepository weeklyPlanRepository;

    private ScheduledWorkoutRepository scheduledWorkoutRepository;
    private WorkoutExerciseRepository workoutExerciseRepository;

    private ExerciseRepository exerciseRepository;

//    keeping this method just in case
//    private WorkoutExerciseDto mapScheduledToDto(ScheduledWorkoutExercise swe) {
//        WorkoutExercise we = swe.getWorkoutExercise();
//
//        Exercise exercise = null;
//
//        if (we != null && we.getExerciseId() != null) {
//            exercise = exerciseRepository.findById(we.getExerciseId()).orElse(null);
//        }
//        else if (swe.getExerciseId() != null) {
//            exercise = exerciseRepository.findById(swe.getExerciseId()).orElse(null);
//        }
//
//        List<WorkoutSetDto> sets = swe.getSets().stream()
//                .map(setLog -> new WorkoutSetDto(
//                        setLog.getSetLogId(),
//                        setLog.getSetNumber(),
//                        setLog.getActualReps(),
//                        setLog.getActualWeight()
//                ))
//                .sorted(Comparator.comparingInt(WorkoutSetDto::setNumber))
//                .toList();
//
//        return new WorkoutExerciseDto(
//                (we != null) ? we.getWorkoutExerciseId() : null,
//                (we != null) ? we.getExerciseId() : swe.getExerciseId(),
//                swe.getExerciseOrder(),
//                (exercise != null) ? exercise.getExerciseName() : "Unnamed Exercise",
//                sets,
//                swe.getSupersetGroupId()
//        );
//    }

    @Override
    public List<CalendarEntryDto> getScheduledWorkouts(UUID userId, LocalDate from, LocalDate to) {

        Optional<WeeklyPlan> optionalPlan =
                weeklyPlanRepository.findWeeklyPlanByPlanActiveIsAndUserId(true, userId);


        List<ScheduledWorkout> scheduled = scheduledWorkoutRepository
                .findByWorkout_UserIdAndWorkoutScheduledDateBetween(userId, from, to);


        Map<LocalDate, ScheduledWorkout> scheduledByDate = scheduled.stream()
                .collect(Collectors.toMap(
                        ScheduledWorkout::getWorkoutScheduledDate,
                        sw -> sw,
                        (sw1, sw2) -> sw1
                ));

            List<CalendarEntryDto> result = new ArrayList<>();


        LocalDate current = from;
        while (!current.isAfter(to)) {
            DayOfWeek dow = current.getDayOfWeek();


            if (scheduledByDate.containsKey(current)) {
                ScheduledWorkout sw = scheduledByDate.get(current);

                result.add(CalendarEntryDto.builder()
                        .scheduledWorkoutId(sw.getScheduledWorkoutId())
                        .workoutId(sw.getWorkout().getWorkoutId())
                        .workoutName(sw.getWorkout().getWorkoutName())
                        .workoutScheduledDate(sw.getWorkoutScheduledDate())
                        .status(sw.getWorkoutStatus())
                        .isVirtual(false)
                        .build());
            }

            else if (optionalPlan.isPresent()) {
                WeeklyPlan plan = optionalPlan.get();

                List<WeeklyPlanEntry> entriesForDay = plan.getEntries().stream()
                        .filter(e -> e.getDayOfWeek().equals(dow))
                        .toList();

                for (WeeklyPlanEntry entry : entriesForDay) {

                    result.add(CalendarEntryDto.builder()
                            .scheduledWorkoutId(null)
                            .workoutId(entry.getWorkout().getWorkoutId())
                            .workoutName(entry.getWorkout().getWorkoutName())
                            .workoutScheduledDate(current)
                            .status(WorkoutStatus.SCHEDULED)
                            .isVirtual(true)
                            .build());
                }
            }

            current = current.plusDays(1);
        }

        return result;
    }

    @Override
    public CalendarWorkoutDto getScheduledWorkout(UUID scheduledWorkoutId){
        ScheduledWorkout sw = scheduledWorkoutRepository.findById(scheduledWorkoutId)
                .orElseThrow(() -> new AppException("Scheduled workout not found", HttpStatus.NOT_FOUND));

        List<ScheduledWorkoutExercise> exercises = sw.getExercises().stream()
                .sorted(Comparator.comparingInt(ScheduledWorkoutExercise::getExerciseOrder))
                .peek(ex -> ex.setSets(
                        ex.getSets().stream()
                                .sorted(Comparator.comparingInt(SetLog::getSetNumber))
                                .toList()
                ))
                .toList();

        return CalendarWorkoutDto.builder()
                .scheduledWorkoutId(sw.getScheduledWorkoutId())
                .workoutId(sw.getWorkout().getWorkoutId())
                .workoutName(sw.getWorkout().getWorkoutName())
                .workoutDescription(sw.getWorkoutNotes())
                .workoutScheduledDate(sw.getWorkoutScheduledDate())
                .workoutPoints(sw.getWorkoutPoints())
                .startedAt(sw.getWorkoutStartedAt())
                .completedAt(sw.getWorkoutCompletedAt())
                .exercises(exercises)
                .status(sw.getWorkoutStatus())
                .isVirtual(false)
                .build();
    }

    @Override
    public ScheduledWorkout generateScheduledWorkout(UUID userId, UUID workoutId, LocalDate date, WorkoutStatus status){
        ScheduledWorkout sw = scheduledWorkoutRepository.findByWorkout_UserIdAndWorkoutScheduledDate(userId, date);

        if(sw != null){
            scheduledWorkoutRepository.delete(sw);
        }

        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new AppException("Workout not found", HttpStatus.NOT_FOUND));

        List<WorkoutExercise> workoutExercises = workoutExerciseRepository
                .findByWorkout_WorkoutIdOrderByExerciseOrderAsc(workoutId);

        ScheduledWorkout scheduledWorkout = ScheduledWorkout.builder()
                .workout(workout)
                .workoutScheduledDate(date)
                .workoutNotes(workout.getWorkoutDescription())
                .muscleGroups(workout.getMuscleGroups())
                .workoutPoints(0)
                .workoutStatus(status)
                .build();

        List<ScheduledWorkoutExercise> scheduledExercises = workoutExercises.stream()
                .map(we -> {
                    Exercise exercise = exerciseRepository.findById(we.getExerciseId()).orElse(null);

                    ScheduledWorkoutExercise swe = new ScheduledWorkoutExercise();
                    swe.setScheduledWorkout(scheduledWorkout);
                    swe.setWorkoutExercise(we);
                    swe.setExerciseId(we.getExerciseId());
                    swe.setExerciseName(exercise.getExerciseName());
                    swe.setExerciseOrder(we.getExerciseOrder());
                    swe.setSupersetGroupId(we.getSupersetGroupId());
                    swe.setSkipped(false);
                    swe.setCompleted(false);

                    List<SetLog> logs = we.getSets().stream()
                            .map(ws -> {
                                SetLog log = new SetLog();
                                log.setScheduledWorkoutExercise(swe);
                                log.setSetNumber(ws.getSetNumber());
                                log.setActualReps(ws.getReps());
                                log.setActualWeight(ws.getWeight());
                                return log;
                            })
                            .collect(Collectors.toList());

                    swe.setSets(logs);
                    return swe;
                })
                .collect(Collectors.toList());

        scheduledWorkout.setExercises(scheduledExercises);

        return scheduledWorkoutRepository.save(scheduledWorkout);
    }

    @Override
    @Transactional
    public void changeWorkoutStatus(UUID scheduledWorkoutId, WorkoutStatus status){
        ScheduledWorkout sw = scheduledWorkoutRepository.findById(scheduledWorkoutId)
                .orElseThrow(() -> new AppException("Scheduled workout not found", HttpStatus.NOT_FOUND));

        sw.setWorkoutStatus(status);
        scheduledWorkoutRepository.save(sw);
    }

    @Override
    @Transactional
    public void updateScheduledWorkout(UUID scheduledWorkoutId, ScheduledWorkoutUpdateDto dto){

        ScheduledWorkout sw = scheduledWorkoutRepository.findById(scheduledWorkoutId)
                .orElseThrow(() -> new AppException("Scheduled workout not found", HttpStatus.NOT_FOUND));

        sw.setWorkoutNotes(dto.workoutNotes());
        sw.setWorkoutStatus(dto.status());
        sw.setWorkoutStartedAt(dto.startedAt());
        sw.setWorkoutCompletedAt(dto.completedAt());
        sw.setMuscleGroups(dto.muscleGroups());
        sw.setWorkoutPoints(dto.workoutPoints());

        //this handles supersets when TEMP is sent in from fe it becomes a group
        Map<String, UUID> resolvedGroups = new HashMap<>();
        for (int i = 0; i < dto.exercises().size(); i++) {
            ScheduledExerciseUpdateDto ex = dto.exercises().get(i);
            String group = ex.supersetGroupId();

            if (group != null && group.startsWith("TEMP-")) {
                resolvedGroups.putIfAbsent(group, UUID.randomUUID());
            }
        }

        for (int i = 0; i < dto.exercises().size(); i++) {
            ScheduledExerciseUpdateDto ex = dto.exercises().get(i);
            String group = ex.supersetGroupId();

            if (group != null && group.startsWith("TEMP-")) {
                UUID resolved = resolvedGroups.get(group);
                dto.exercises().set(i,
                        ex.withSupersetGroupId(ex, resolved.toString())
                );
            }
        }

        for (ScheduledExerciseUpdateDto exDto : dto.exercises()) {

            if (exDto.toDelete()) {
                sw.getExercises().removeIf(e -> e.getScheduledWorkoutExerciseId().equals(exDto.scheduledWorkoutExerciseId()));
                continue;
            }

            ScheduledWorkoutExercise swe;

            if (exDto.scheduledWorkoutExerciseId() != null) {
                swe = sw.getExercises().stream()
                        .filter(e -> e.getScheduledWorkoutExerciseId().equals(exDto.scheduledWorkoutExerciseId()))
                        .findFirst()
                        .orElseThrow(() -> new AppException("Exercise not found", HttpStatus.NOT_FOUND));
            } else {
                swe = new ScheduledWorkoutExercise();
                swe.setScheduledWorkout(sw);
                sw.getExercises().add(swe);
            }

            if(exDto.workoutExerciseId() != null){
                swe.setWorkoutExercise(
                        workoutExerciseRepository.findById(exDto.workoutExerciseId())
                                .orElseThrow(() -> new AppException("Exercise not found", HttpStatus.NOT_FOUND))
                );
            }
            else{
                swe.setWorkoutExercise(null);
                swe.setExerciseId(exDto.exerciseId());
            }
            swe.setExerciseName(exDto.exerciseName());
            swe.setExerciseOrder(exDto.exerciseOrder());
            swe.setSkipped(exDto.toSkip());
            swe.setCompleted(exDto.toComplete());

            if (exDto.supersetGroupId() != null) {
                swe.setSupersetGroupId(UUID.fromString(exDto.supersetGroupId()));
            } else {
                swe.setSupersetGroupId(null);
            }

            if(!swe.isSkipped()) {
                for (SetLogUpdateDto setDto : exDto.sets()) {
                    if (setDto.toDelete()) {
                        swe.getSets().removeIf(s -> s.getSetLogId() != null && s.getSetLogId().equals(setDto.setLogId()));
                        continue;
                    }

                    SetLog setLog = (setDto.setLogId() != null)
                            ? swe.getSets().stream()
                            .filter(s -> s.getSetLogId() != null && s.getSetLogId().equals(setDto.setLogId()))
                            .findFirst()
                            .orElse(new SetLog())
                            : new SetLog();

                    setLog.setScheduledWorkoutExercise(swe);
                    setLog.setSetNumber(setDto.setNumber());
                    setLog.setActualReps(setDto.actualReps());
                    setLog.setActualWeight(setDto.actualWeight());
                    setLog.setCompleted(setDto.toComplete());
                    setLog.setSkipped(setDto.toSkip());
                    setLog.setSetPoints(setDto.setPoints());

                    swe.getSets().add(setLog);
                }
            }else{swe.getSets().clear();}
        }

        scheduledWorkoutRepository.save(sw);
    }

    @Override
    @Transactional
    public void refreshAllStatuses(UUID userId) {
        List<ScheduledWorkout> workouts = scheduledWorkoutRepository.findAllByWorkout_UserId(userId);

        workouts.forEach(sw -> {
            WorkoutStatus currentStatus = sw.getWorkoutStatus();

            if (currentStatus == WorkoutStatus.COMPLETED || currentStatus == WorkoutStatus.PAUSED) {
                sw.setWorkoutStatus(WorkoutStatus.COMPLETED);
            }
            else if(sw.getWorkoutScheduledDate().isBefore(LocalDate.now())) {
                sw.setWorkoutStatus(WorkoutStatus.SKIPPED);
            }
        });

        scheduledWorkoutRepository.saveAll(workouts);
    }

    @Override
    public void deleteScheduledWorkout(UUID scheduledWorkoutId){
        scheduledWorkoutRepository.deleteById(scheduledWorkoutId);
    }
}
