package com.antonk.gymtracker.entity;

import com.antonk.gymtracker.entity.enums.MuscleGroup;
import com.antonk.gymtracker.entity.enums.UnitPreference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "workouts")
public class Workout {

    @Id
    @GeneratedValue
    private UUID workoutId;


    @Column(name = "user_id", nullable = false)
    private UUID userId;

    private String workoutName;

    private String workoutDescription;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<WorkoutExercise> exercises = new ArrayList<>();
    private List<MuscleGroup> muscleGroups = new ArrayList<>();

    public Workout(
                String workoutName,
                String workoutDescription,
                LocalDateTime createdAt,
                List<MuscleGroup> muscleGroups) {
        this.workoutName = workoutName;
        this.workoutDescription = workoutDescription;
        this.createdAt = LocalDateTime.now();
    }
}
