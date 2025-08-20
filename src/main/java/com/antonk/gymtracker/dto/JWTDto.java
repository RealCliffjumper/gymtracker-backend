package com.antonk.gymtracker.dto;

import com.antonk.gymtracker.entity.User;

public record JWTDto(
        User user,
        String token) {
}
