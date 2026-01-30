package com.antonk.gymtracker.dto;

import java.util.UUID;

public record UpdateEntryReq(
        UUID workoutId
) {
}
