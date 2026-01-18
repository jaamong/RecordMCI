package io.jaamong.recordMCI.api.dto.request.activity;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record ActivityNameUpdateRequest(
        @NotBlank
        String name
) {
    @Builder
    public ActivityNameUpdateRequest {
    }

    public ActivityNameUpdateServiceRequest toServiceRequest(Long id) {
        return ActivityNameUpdateServiceRequest.builder()
                .id(id)
                .name(this.name)
                .build();
    }
}
