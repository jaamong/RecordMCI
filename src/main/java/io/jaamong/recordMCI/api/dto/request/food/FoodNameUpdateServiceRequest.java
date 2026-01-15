package io.jaamong.recordMCI.api.dto.request.food;

import lombok.Builder;

public record FoodNameUpdateServiceRequest(
        Long id,
        String name
) {

    @Builder
    public FoodNameUpdateServiceRequest {
    }
}
