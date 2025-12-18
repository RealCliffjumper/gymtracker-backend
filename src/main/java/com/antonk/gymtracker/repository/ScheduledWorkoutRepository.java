package com.antonk.gymtracker.repository;

import com.antonk.gymtracker.entity.ScheduledWorkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ScheduledWorkoutRepository extends JpaRepository<ScheduledWorkout, UUID> {

    List<ScheduledWorkout> findByWorkout_UserIdAndWorkoutScheduledDateBetween(UUID userId, LocalDate from, LocalDate to);

    List<ScheduledWorkout> findAllByWorkout_UserId(UUID userId);

    ScheduledWorkout findByWorkout_UserIdAndWorkoutScheduledDate(UUID userId, LocalDate date);
}
