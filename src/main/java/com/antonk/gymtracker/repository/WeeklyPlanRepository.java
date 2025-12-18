package com.antonk.gymtracker.repository;

import com.antonk.gymtracker.entity.WeeklyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WeeklyPlanRepository extends JpaRepository<WeeklyPlan, UUID> {
    Optional<WeeklyPlan> findWeeklyPlanByPlanActiveIsAndUserId(boolean planActive, UUID userId);

    List<WeeklyPlan> findByUserId(UUID userId);
}
