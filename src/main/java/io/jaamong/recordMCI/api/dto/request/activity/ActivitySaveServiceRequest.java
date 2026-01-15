package io.jaamong.recordMCI.api.dto.request.activity;

import lombok.Builder;

public record ActivitySaveServiceRequest(
        Long dailyRecordId,
        String name
) {

    @Builder
    public ActivitySaveServiceRequest {
    }
}
