package io.jaamong.recordMCI.api.dto.request.activity;

import lombok.Builder;

public record ActivityNameUpdateServiceRequest(
        Long id,
        String name
) {

    @Builder
    public ActivityNameUpdateServiceRequest {
    }
}
