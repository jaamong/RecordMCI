package io.jaamong.recordMCI.api.dto.request;

import io.jaamong.recordMCI.domain.entity.NutrientType;
import lombok.Builder;

public record FoodSaveServiceRequest(
        Long dailyRecordId,
        String name,
        NutrientType nutrientType
) {

    @Builder
    public FoodSaveServiceRequest {
    }
}
