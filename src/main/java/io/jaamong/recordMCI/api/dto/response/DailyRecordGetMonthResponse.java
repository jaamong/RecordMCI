package io.jaamong.recordMCI.api.dto.response;

import lombok.Builder;

import java.time.LocalDate;

public record DailyRecordGetMonthResponse(
        LocalDate date,
        Boolean hasRecord,
        Boolean hasFoodCompleted,
        Boolean hasActivityCompleted,
        Boolean hasMemo
) {

    @Builder
    public DailyRecordGetMonthResponse {
    }
}
