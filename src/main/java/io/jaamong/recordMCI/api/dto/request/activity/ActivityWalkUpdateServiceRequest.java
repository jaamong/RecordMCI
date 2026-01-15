package io.jaamong.recordMCI.api.dto.request.activity;

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
