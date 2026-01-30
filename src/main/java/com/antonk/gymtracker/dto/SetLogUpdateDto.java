package com.antonk.gymtracker.dto;

import java.util.UUID;

public record SetLogUpdateDto(
        UUID setLogId,
        int setNumber,
        int actualReps,
        int actualWeight,
        int setPoints,
        boolean toDelete,
        boolean toComplete,
        boolean toSkip
) {
}
