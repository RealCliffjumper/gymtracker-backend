package com.antonk.gymtracker.dto;

import com.antonk.gymtracker.entity.enums.UnitPreference;

import java.util.UUID;

public record UserFetchDto(
        UUID userId,
        String userFirstName,
        String userLastName,
        String userLoginId,
        UnitPreference unitPreference
) {
}
