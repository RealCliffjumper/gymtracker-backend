package com.antonk.gymtracker.dto;

import com.antonk.gymtracker.entity.enums.UnitPreference;

import java.time.LocalDate;
import java.util.UUID;

public record UserFetchDto(
        UUID userId,
        String userFirstName,
        String userLastName,
        String userLoginId,
        LocalDate lastLoggedIn,
        UnitPreference unitPreference
) {
}
