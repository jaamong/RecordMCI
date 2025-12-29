package io.jaamong.recordMCI.domain.dto;

import io.jaamong.recordMCI.domain.entity.NutrientType;
import lombok.Builder;

public record Food(
        Long id,
        Long dailyRecordId,
        String name,
        Boolean consumed,
        NutrientType nutrientType) {

    @Builder
    public Food {
    }
}
