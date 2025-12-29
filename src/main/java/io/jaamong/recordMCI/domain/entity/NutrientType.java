package io.jaamong.recordMCI.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
