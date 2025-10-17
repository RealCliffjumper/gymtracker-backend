package com.antonk.gymtracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "weekly_plans")
public class WeeklyPlan {

    @Id
    @GeneratedValue
    private UUID weeklyPlanId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    private String weeklyPlanName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private boolean planActive = false;

    @OneToMany(mappedBy = "weeklyPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeeklyPlanEntry> entries = new ArrayList<>();

    public WeeklyPlan(
            String weeklyPlanName
    ){
        this.weeklyPlanName = weeklyPlanName;
        this.createdAt = LocalDateTime.now();
    }
}
