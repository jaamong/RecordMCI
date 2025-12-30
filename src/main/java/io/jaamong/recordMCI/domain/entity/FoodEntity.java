package io.jaamong.recordMCI.domain.entity;

import io.jaamong.recordMCI.domain.dto.Food;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "food")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodEntity extends BaseEntity {

    @Column(name = "food_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_record_id")
    private DailyRecordEntity dailyRecordEntity;

    private String name;
    private Boolean consumed;

    @Enumerated(EnumType.STRING)
    private NutrientType nutrientType;  // nullable

    @Builder
    public FoodEntity(DailyRecordEntity dailyRecordEntity, String name, Boolean consumed, NutrientType nutrientType) {
        this.dailyRecordEntity = dailyRecordEntity;
        this.name = name;
        this.consumed = consumed;
        this.nutrientType = nutrientType;
    }

    public static FoodEntity of(DailyRecordEntity dailyRecordEntity, NutrientType nutrientType, String name) {
        return builder()
                .dailyRecordEntity(dailyRecordEntity)
                .nutrientType(nutrientType)
                .name(name)
                .consumed(false)
                .build();
    }

    @Override
    public String toString() {
        return "FoodEntity{" +
                "id=" + id +
                ", dailyRecord.id=" + dailyRecordEntity.getId() +
                ", name='" + name + '\'' +
                ", consumed=" + consumed +
                ", nutrientType.name=" + nutrientType.name() +
                '}';
    }

    public void mapDailyRecord(DailyRecordEntity dailyRecordEntity) {
        this.dailyRecordEntity = dailyRecordEntity;
    }

    public void invertConsumed() {
        this.consumed = !this.consumed;
    }

    public Food toModel() {
        return Food.builder()
                .id(id)
                .name(name)
                .nutrientType(nutrientType)
                .dailyRecordId(dailyRecordEntity.getId())
                .consumed(consumed)
                .build();
    }
}
