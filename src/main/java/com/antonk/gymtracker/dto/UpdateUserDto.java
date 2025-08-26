package com.antonk.gymtracker.dto;

import com.antonk.gymtracker.entity.enums.UnitPreference;

public record UpdateUserDto(String userLoginId,
                            String userFirstName,
                            String userLastName,
                            UnitPreference unitPreference) {
}
