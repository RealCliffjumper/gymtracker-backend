package com.antonk.gymtracker.dto;

import com.antonk.gymtracker.entity.enums.UnitPreference;

import java.time.LocalDateTime;

public record SignUpDto(
        String userLoginId,
        String userFirstName,
        String userLastName,
        String password,
        LocalDateTime createdAt,
        UnitPreference unitPreference) {
}
