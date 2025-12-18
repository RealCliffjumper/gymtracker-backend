package com.antonk.gymtracker.dto;

import java.util.List;
import java.util.UUID;

public record ScheduledExerciseUpdateDto (
        UUID scheduledWorkoutExerciseId, // null if newly added
        UUID scheduledWorkoutId,
        UUID workoutExerciseId, //also null if added from scheduled
        UUID exerciseId, //if no workoutExercise relation this gets set
        String exerciseName,
        int exerciseOrder,
        String supersetGroupId,
        boolean toDelete,// optional flag if the exercise should be removed
        boolean toSuperset,
        boolean toUnlink,
        boolean toSkip,
        boolean toComplete,
        List<SetLogUpdateDto> sets
){
    public ScheduledExerciseUpdateDto withSupersetGroupId(
            ScheduledExerciseUpdateDto dto,
            String newGroupId
    ) {
        return new ScheduledExerciseUpdateDto(
                dto.scheduledWorkoutExerciseId(),
                dto.scheduledWorkoutId(),
                dto.workoutExerciseId(),
                dto.exerciseId(),
                dto.exerciseName(),
                dto.exerciseOrder(),
                newGroupId,
                dto.toDelete(),
                dto.toUnlink(),
                dto.toSuperset(),
                dto.toSkip(),
                dto.toComplete(),
                dto.sets()
        );
    }
}
