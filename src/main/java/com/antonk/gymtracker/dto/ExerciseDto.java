package com.antonk.gymtracker.dto;

import com.antonk.gymtracker.entity.enums.Equipment;
import com.antonk.gymtracker.entity.enums.MuscleGroup;

public record ExerciseDto(
        String exerciseName,
        String exerciseDescription,
        MuscleGroup muscleGroup,
        Equipment equipment
) {
}
