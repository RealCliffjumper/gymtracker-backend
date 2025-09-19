package com.antonk.gymtracker.dto;

import java.util.UUID;

public record SupersetRequest(
        UUID id1,
        UUID id2
) {
}
