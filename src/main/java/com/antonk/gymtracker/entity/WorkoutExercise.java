package com.antonk.gymtracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "workout_exercises")
public class WorkoutExercise {
    @Id
    @GeneratedValue
    private UUID workoutExerciseId;

    @Column(name = "exercise_id")
    private UUID exerciseId;

    @Column(name = "workout_id")
    private UUID workoutId;

    private int exerciseOrder;

    @OneToMany(mappedBy = "workoutExercise", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WorkoutSet> sets = new ArrayList<>();

    private UUID supersetGroupId;
}
