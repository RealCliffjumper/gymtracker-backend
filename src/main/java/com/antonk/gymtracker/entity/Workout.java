package com.antonk.gymtracker.entity;

import com.antonk.gymtracker.entity.enums.UnitPreference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    public Workout(
                String workoutName,
                String workoutDescription,
                LocalDateTime createdAt) {
        this.workoutName = workoutName;
        this.workoutDescription = workoutDescription;
        this.createdAt = LocalDateTime.now();
    }
}
