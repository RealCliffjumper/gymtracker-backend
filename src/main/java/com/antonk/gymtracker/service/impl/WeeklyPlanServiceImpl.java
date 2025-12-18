package com.antonk.gymtracker.service.impl;

import com.antonk.gymtracker.dto.PlanStatusDto;
import com.antonk.gymtracker.dto.PlanUpdateDtoReq;
import com.antonk.gymtracker.entity.WeeklyPlan;
import com.antonk.gymtracker.exception.AppException;
import com.antonk.gymtracker.repository.WeeklyPlanRepository;
import com.antonk.gymtracker.service.WeeklyPlanService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
class WeeklyPlanServiceImpl implements WeeklyPlanService {
    private WeeklyPlanRepository weeklyPlanRepository;

    @Override
    public List<WeeklyPlan> getWeeklyPlans(UUID userId) {
        return weeklyPlanRepository.findByUserId(userId);
    }

    @Override
    public WeeklyPlan getWeeklyPlan (UUID planId) {
        return weeklyPlanRepository.findById(planId)
                .orElse(null);
    }

    @Override
    public WeeklyPlan getActivePlan(UUID userId){
        return weeklyPlanRepository.findWeeklyPlanByPlanActiveIsAndUserId(true, userId)
                .orElse(null);
    }

    @Override
    public WeeklyPlan createWeeklyPlan(UUID userId, String weeklyPlanName){
        WeeklyPlan weeklyPlan = new WeeklyPlan(
                weeklyPlanName
        );
        weeklyPlan.setUserId(userId);
        weeklyPlan.setCreatedAt(LocalDateTime.now());
        weeklyPlan.setUpdatedAt(LocalDateTime.now());

        return weeklyPlanRepository.save(weeklyPlan);
    }

    @Override
    public WeeklyPlan updateWeeklyPlan(UUID weeklyPlanId, PlanUpdateDtoReq req){
        WeeklyPlan weeklyPlan = getWeeklyPlan(weeklyPlanId);

        weeklyPlan.setWeeklyPlanName(req.weeklyPlanName());
        weeklyPlan.setUpdatedAt(LocalDateTime.now());
        return weeklyPlanRepository.save(weeklyPlan);
    }

    @Override
    public WeeklyPlan changeWeeklyPlanStatus(UUID weeklyPlanId, PlanStatusDto dto){ //until batch update its a separate method
        WeeklyPlan weeklyPlan = getWeeklyPlan(weeklyPlanId);
        List<WeeklyPlan> activePlans = weeklyPlanRepository.findByUserId(dto.userId());

        activePlans.forEach(
                activePlan -> {
                    activePlan.setPlanActive(false);
                });

        weeklyPlanRepository.saveAll(activePlans);
        weeklyPlan.setPlanActive(dto.planActive());
        return weeklyPlanRepository.save(weeklyPlan);
    }

    @Override
    public void deleteWeeklyPlan (UUID weeklyPlanId) {
        weeklyPlanRepository.deleteById(weeklyPlanId);
    }

    @Override
    public void deleteWeeklyPlans (UUID userId) {
        List<WeeklyPlan> plans = weeklyPlanRepository.findByUserId(userId);
        weeklyPlanRepository.deleteAll(plans);
    }
}
