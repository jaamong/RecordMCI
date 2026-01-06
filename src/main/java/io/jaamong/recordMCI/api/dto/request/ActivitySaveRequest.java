package io.jaamong.recordMCI.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record ActivitySaveRequest(
    Long dailyRecordId,
    @NotBlank
    String name
) {

    @Builder
    public ActivitySaveRequest {
    }

    public ActivitySaveServiceRequest toServiceRequest() {
        return ActivitySaveServiceRequest.builder()
                .dailyRecordId(this.dailyRecordId)
                .name(this.name)
                .build();
    }
}
