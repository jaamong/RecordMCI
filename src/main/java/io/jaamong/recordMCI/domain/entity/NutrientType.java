package io.jaamong.recordMCI.domain.entity;

import io.jaamong.recordMCI.api.exception.CustomRuntimeException;
import io.jaamong.recordMCI.api.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum NutrientType {
    CARBOHYDRATES("탄수화물"),
    CARBO_VEGETABLE("채소"),
    CARBO_FRUIT("과일"),
    PROTEIN("단백질"),
    FAT("지방");

    final private String type;

}
