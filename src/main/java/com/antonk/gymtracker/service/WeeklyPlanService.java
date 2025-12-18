package com.antonk.gymtracker.service;

import com.antonk.gymtracker.dto.PlanStatusDto;
import com.antonk.gymtracker.dto.PlanUpdateDtoReq;
import com.antonk.gymtracker.entity.WeeklyPlan;

import java.util.List;
import java.util.UUID;

public interface WeeklyPlanService {

    List<WeeklyPlan> getWeeklyPlans (UUID userId);
    WeeklyPlan getWeeklyPlan (UUID planId);
    WeeklyPlan getActivePlan(UUID userId);
    WeeklyPlan createWeeklyPlan(UUID userId, String planName);
    WeeklyPlan updateWeeklyPlan(UUID weeklyPlanId, PlanUpdateDtoReq  planUpdateDtoReq);
    WeeklyPlan changeWeeklyPlanStatus (UUID weeklyPlanId, PlanStatusDto planStatusDto);
    void deleteWeeklyPlan (UUID weeklyPlanId);
    void deleteWeeklyPlans (UUID userId);
}
