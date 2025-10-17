package com.antonk.gymtracker.dto;

import java.util.UUID;

public record PlanStatusDto(
        UUID userId,
        boolean planActive
) {
}
