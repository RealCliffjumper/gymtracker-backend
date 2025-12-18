package com.antonk.gymtracker.controller;

import com.antonk.gymtracker.dto.CalendarEntryDto;
import com.antonk.gymtracker.dto.CalendarWorkoutDto;
import com.antonk.gymtracker.dto.CalendarWorkoutReq;
import com.antonk.gymtracker.dto.ScheduledWorkoutUpdateDto;
import com.antonk.gymtracker.entity.ScheduledWorkout;
import com.antonk.gymtracker.entity.enums.WorkoutStatus;
import com.antonk.gymtracker.service.ScheduledWorkoutService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/scheduled")
class ScheduleController {

    private final ScheduledWorkoutService scheduledWorkoutService;

    @GetMapping(path = "{userId}/all")
    public ResponseEntity<List<CalendarEntryDto>> getCalendarWorkouts(
            @PathVariable UUID userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        List<CalendarEntryDto> workouts = scheduledWorkoutService.getScheduledWorkouts(userId, from, to);
        return ResponseEntity.ok(workouts);
    }

    @GetMapping(path = "get/{scheduledWorkoutId}")
    public CalendarWorkoutDto  getScheduledWorkout(@PathVariable UUID scheduledWorkoutId) {
        return scheduledWorkoutService.getScheduledWorkout(scheduledWorkoutId);
    }

    @PostMapping(path = "{userId}/add")
    public ResponseEntity<?> addScheduledEntry(@PathVariable UUID userId, @RequestBody CalendarWorkoutReq calendarWorkoutReq){
       ScheduledWorkout sw = scheduledWorkoutService.generateScheduledWorkout(userId, calendarWorkoutReq.workoutId(), calendarWorkoutReq.workoutScheduledDate(), calendarWorkoutReq.workoutStatus());
       return ResponseEntity.ok(sw);
    }

    @PutMapping(path = "{scheduledWorkoutId}/status")
    public ResponseEntity<Void> changeWorkoutStatus(@PathVariable UUID scheduledWorkoutId, @RequestBody WorkoutStatus status){
        scheduledWorkoutService.changeWorkoutStatus(scheduledWorkoutId, status);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "{scheduledWorkoutId}/update")
    public ResponseEntity<Void> updateScheduledWorkout(@PathVariable UUID scheduledWorkoutId, @RequestBody ScheduledWorkoutUpdateDto updatedScheduledWorkout){
        scheduledWorkoutService.updateScheduledWorkout(scheduledWorkoutId, updatedScheduledWorkout);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "{userId}/refresh")
    public void refreshStatuses(@PathVariable UUID userId){
        scheduledWorkoutService.refreshAllStatuses(userId);
    }

    @DeleteMapping(path = "{scheduledWorkoutId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteScheduledWorkout(@PathVariable UUID scheduledWorkoutId){
        scheduledWorkoutService.deleteScheduledWorkout(scheduledWorkoutId);
    }
}
