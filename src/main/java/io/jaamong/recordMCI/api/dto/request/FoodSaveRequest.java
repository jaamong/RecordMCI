package io.jaamong.recordMCI.api.dto.request;

import io.jaamong.recordMCI.domain.entity.NutrientType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

public record FoodSaveRequest(
        Long dailyRecordId,
        @NotBlank
        String name,
        @NotEmpty
        String nutrientType
) {

    @Builder
    public FoodSaveRequest {
    }

    public FoodSaveServiceRequest toServiceRequest() {
        return FoodSaveServiceRequest.builder()
                .dailyRecordId(this.dailyRecordId)
                .name(this.name)
                .nutrientType(NutrientType.of(this.nutrientType))
                .build();
    }
}
