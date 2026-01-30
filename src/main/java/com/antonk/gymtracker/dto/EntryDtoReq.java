package com.antonk.gymtracker.dto;


import java.time.DayOfWeek;
import java.util.UUID;

public record EntryDtoReq(
        UUID workoutId,
        DayOfWeek dow
) {
}
