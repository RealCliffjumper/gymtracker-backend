package com.antonk.gymtracker.entity;

import com.antonk.gymtracker.entity.enums.Equipment;
import com.antonk.gymtracker.entity.enums.MuscleGroup;
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
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue
    private UUID exerciseId;

    @Column(name = "user_id")
    private UUID userId;

    private String exerciseName;
    private String exerciseDescription;
    @Enumerated(EnumType.STRING)
    private MuscleGroup muscleGroup;
    @Enumerated(EnumType.STRING)
    private Equipment equipment;
    private boolean isPublic = true;


    public Exercise(
            String exerciseName,
            String exerciseDescription,
            MuscleGroup muscleGroup,
            Equipment equipment) {
        this.exerciseName = exerciseName;
        this.exerciseDescription = exerciseDescription;
        this.muscleGroup = muscleGroup;
        this.equipment = equipment;
    }
}
