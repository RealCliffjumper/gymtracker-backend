package com.antonk.gymtracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "workout_sets")
public class WorkoutSet {
    @Id
    @GeneratedValue
    private UUID workoutSetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    @JsonIgnore
    private WorkoutExercise workoutExercise;

    private int setNumber;
    private int reps;
    private int weight;

    public WorkoutSet(int i, int reps, int weight) {
    }
}
