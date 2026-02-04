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
@Table(name = "set_logs")
public class SetLog {

    @Id
    @GeneratedValue
    private UUID setLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduled_workout_exercise_id", nullable = false)
    @JsonIgnore
    private ScheduledWorkoutExercise scheduledWorkoutExercise;

    private int setNumber;
    private int actualReps;
    private int actualWeight;
    private boolean isCompleted = false;
    private boolean isSkipped = false;
    private double setPoints = 0.0;

    public SetLog(int i, int reps, int weight) {
    }
}
