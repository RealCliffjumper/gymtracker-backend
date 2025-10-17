package com.antonk.gymtracker.repository;

import com.antonk.gymtracker.entity.WeeklyPlan;
import com.antonk.gymtracker.entity.WeeklyPlanEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WeeklyPlanEntryRepository extends JpaRepository<WeeklyPlanEntry, UUID> {

    List<WeeklyPlanEntry> findByWeeklyPlan_WeeklyPlanId(UUID weeklyPlanId);
}
