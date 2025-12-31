package io.jaamong.recordMCI.api.dto.request;

import lombok.Builder;

public record ActivityWalkUpdateServiceRequest(
        Long id,
        int totalSteps,
        int totalHours,
        int totalMinutes
) {

    @Builder
    public ActivityWalkUpdateServiceRequest {
    }
}
