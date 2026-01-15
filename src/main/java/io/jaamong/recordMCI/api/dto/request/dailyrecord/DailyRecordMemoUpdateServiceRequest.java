package io.jaamong.recordMCI.api.dto.request.dailyrecord;

import lombok.Builder;

public record DailyRecordMemoUpdateServiceRequest(
        Long id,
        String memo
) {
    @Builder
    public DailyRecordMemoUpdateServiceRequest {
    }
}
