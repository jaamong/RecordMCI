package io.jaamong.recordMCI.api.dto.request;

import lombok.Builder;

public record ActivitySaveServiceRequest(
        Long dailyRecordId,
        String name
) {

    @Builder
    public ActivitySaveServiceRequest {
    }
}
