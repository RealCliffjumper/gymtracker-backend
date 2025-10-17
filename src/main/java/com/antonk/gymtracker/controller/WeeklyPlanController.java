package com.antonk.gymtracker.controller;

import com.antonk.gymtracker.dto.*;
import com.antonk.gymtracker.entity.WeeklyPlan;
import com.antonk.gymtracker.entity.WeeklyPlanEntry;
import com.antonk.gymtracker.service.WeeklyPlanEntryService;
import com.antonk.gymtracker.service.WeeklyPlanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/weekly")
class WeeklyPlanController {

    private WeeklyPlanService weeklyPlanService;
    private WeeklyPlanEntryService weeklyPlanEntryService;

    @GetMapping(path = "{userId}/all")
    public List<WeeklyPlan> getPlans(@PathVariable UUID userId) {
        return weeklyPlanService.getWeeklyPlans(userId);
    }

    @GetMapping(path = "{weeklyPlanId}/find")
    public WeeklyPlan getPlan(@PathVariable UUID weeklyPlanId) {
        return weeklyPlanService.getWeeklyPlan(weeklyPlanId);
    }

    @PostMapping(path = "{userId}/create")
    public ResponseEntity<?> createWeeklyPlan(@PathVariable UUID userId, @RequestBody String weeklyPlanName) {
        WeeklyPlan weeklyPlan = weeklyPlanService.createWeeklyPlan(userId, weeklyPlanName);
        return ResponseEntity.ok(weeklyPlan);
    }

    @PutMapping(path ="{weeklyPlanId}/update")
    public ResponseEntity<WeeklyPlan> updateWeeklyPlan(@PathVariable UUID weeklyPlanId, @RequestBody PlanUpdateDtoReq planUpdateDtoReq) {
        WeeklyPlan weeklyPlan = weeklyPlanService.updateWeeklyPlan(weeklyPlanId, planUpdateDtoReq);
        return ResponseEntity.ok(weeklyPlan);
    }

    @PutMapping(path ="{weeklyPlanId}/status")
    public ResponseEntity<WeeklyPlan> changeWeeklyPlanStatus(@PathVariable UUID weeklyPlanId, @RequestBody PlanStatusDto dto) {
        WeeklyPlan weeklyPlan = weeklyPlanService.changeWeeklyPlanStatus(weeklyPlanId, dto);
        return ResponseEntity.ok(weeklyPlan);
    }

    @DeleteMapping(path = "/delete/{weeklyPlanId}")
    public void deletePlan(@PathVariable UUID weeklyPlanId) {
        weeklyPlanService.deleteWeeklyPlan(weeklyPlanId);
    }

    @GetMapping(path = "/entry/{weeklyPlanId}/all")
    public List<EntryDto> getEntries(@PathVariable UUID weeklyPlanId) {
        return weeklyPlanEntryService.getWeeklyPlanEntries(weeklyPlanId);
    }

    @PostMapping(path = "/entry/{weeklyPlanId}/add")
    public ResponseEntity<?> addWeeklyPlanEntry(@PathVariable UUID weeklyPlanId, @RequestBody EntryDtoReq dto) {
        WeeklyPlanEntry weeklyPlanEntry = weeklyPlanEntryService.addWeeklyPlanEntry(weeklyPlanId, dto.workoutId(), dto.dow());
        return ResponseEntity.ok(weeklyPlanEntry);
    }

    @PutMapping(path = "/entry/{weeklyEntryId}/update")
    public ResponseEntity<WeeklyPlanEntry> updateWeeklyEntry(@PathVariable UUID weeklyEntryId, @RequestBody UpdateEntryReq req){
        WeeklyPlanEntry weeklyPlanEntry = weeklyPlanEntryService.updateWeeklyPlanEntry(weeklyEntryId, req.workoutId());
        return ResponseEntity.ok(weeklyPlanEntry);
    }

    @DeleteMapping(path = "entry/delete/{weeklyEntryId}")
    public void deleteEntry(@PathVariable UUID weeklyEntryId){
        weeklyPlanEntryService.deleteWeeklyPlanEntry(weeklyEntryId);
    }
}
