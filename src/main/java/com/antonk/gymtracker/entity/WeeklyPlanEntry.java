package com.antonk.gymtracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "weekly_plan_entries")
public class WeeklyPlanEntry {

    @Id
    @GeneratedValue
    private UUID weeklyEntryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekly_plan_id", nullable = false)
    @JsonIgnore
    private WeeklyPlan weeklyPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    @JsonIgnore
    private Workout workout;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
}
