package io.jaamong.recordMCI.api.dto.response;

import io.jaamong.recordMCI.domain.dto.Activity;
import io.jaamong.recordMCI.domain.dto.DailyRecord;
import io.jaamong.recordMCI.domain.dto.Food;
import lombok.Builder;

import java.util.List;

public record DailyRecordGetDetailResponse(
        Long id,
        List<Food> foods,
        List<Activity> activities,
        String memo
) {

    @Builder
    public DailyRecordGetDetailResponse {
    }

    public static DailyRecordGetDetailResponse from(DailyRecord dailyRecord) {
        return DailyRecordGetDetailResponse.builder()
                .id(dailyRecord.id())
                .foods(dailyRecord.foods())
                .activities(dailyRecord.activities())
                .memo(dailyRecord.memo())
                .build();
    }
}
