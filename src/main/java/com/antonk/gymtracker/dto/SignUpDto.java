package com.antonk.gymtracker.dto;

import java.time.LocalDateTime;

public record SignUpDto(String userLoginId, String userFirstName, String userLastName, String password, LocalDateTime createdAt) {
}
