package com.antonk.gymtracker.entity;

import com.antonk.gymtracker.entity.enums.MuscleGroup;
import com.antonk.gymtracker.entity.enums.WorkoutStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "scheduled_workouts")
public class ScheduledWorkout {

    @Id
    @GeneratedValue
    private UUID scheduledWorkoutId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    @JsonIgnore
    private Workout workout;

    private LocalDate workoutScheduledDate;
    private LocalDateTime workoutStartedAt;
    private LocalDateTime workoutCompletedAt;
    private String workoutNotes;
    @Enumerated(EnumType.STRING)
    private List<MuscleGroup> muscleGroups;
    private double workoutPoints = 0.0;

    @Enumerated(EnumType.STRING)
    private WorkoutStatus workoutStatus;

    @OneToMany(mappedBy = "scheduledWorkout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduledWorkoutExercise> exercises = new ArrayList<>();

}
