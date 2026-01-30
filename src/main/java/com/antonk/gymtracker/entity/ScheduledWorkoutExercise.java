package com.antonk.gymtracker.entity;

import com.antonk.gymtracker.dto.SetLogUpdateDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "scheduled_workout_exercises")
public class ScheduledWorkoutExercise {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID scheduledWorkoutExerciseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduled_workout_id", nullable = false)
    @JsonIgnore
    private ScheduledWorkout scheduledWorkout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    @JsonIgnore
    private WorkoutExercise workoutExercise;

    private UUID exerciseId;

    private int exerciseOrder;
    private String exerciseName;
    private UUID supersetGroupId;
    private boolean isSkipped = false;
    private boolean isCompleted = false;

    @OneToMany(mappedBy = "scheduledWorkoutExercise", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SetLog> sets = new ArrayList<>();

    public ScheduledWorkoutExercise(
            UUID id,
            ScheduledWorkout sw,
            UUID exId,
            int i,
            String s,
            List<SetLogUpdateDto> sets) {
    }
}
