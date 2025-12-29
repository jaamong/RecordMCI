package io.jaamong.recordMCI.domain.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public record DailyRecord(
        Long id,
        LocalDate date,
        Users user,
        List<Food> foods,
        List<Activity> activities,
        String memo) {

    @Builder
    public DailyRecord {
    }
}
