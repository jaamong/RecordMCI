package io.jaamong.recordMCI.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_record_id")
    private DailyRecord dailyRecord;

    private String name;
    private Boolean consumed;

    @Enumerated(EnumType.STRING)
    private NutrientType nutrientType;  // nullable

    @Builder
    public Food(DailyRecord dailyRecord, String name, Boolean consumed, NutrientType nutrientType) {
        this.dailyRecord = dailyRecord;
        this.name = name;
        this.consumed = consumed;
        this.nutrientType = nutrientType;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", dailyRecord.id=" + dailyRecord.getId() +
                ", name='" + name + '\'' +
                ", consumed=" + consumed +
                ", nutrientType.name=" + nutrientType.name() +
                '}';
    }

    public void mapDailyRecord(DailyRecord dailyRecord) {
        this.dailyRecord = dailyRecord;
    }
}
