package io.jaamong.recordMCI.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record FoodNameUpdateRequest(
        @NotBlank
        String name
) {

    @Builder
    public FoodNameUpdateRequest {
    }


    public FoodNameUpdateServiceRequest toServiceRequest(Long id) {
        return FoodNameUpdateServiceRequest.builder()
                .id(id)
                .name(this.name)
                .build();
    }
}
