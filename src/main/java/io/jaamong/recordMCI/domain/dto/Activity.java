package io.jaamong.recordMCI.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.jaamong.recordMCI.domain.entity.ActivityType;
import lombok.Builder;

public record Activity(
        Long id,
        Long dailyRecordId,
        ActivityType activityType,
        boolean completed,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Integer totalSteps,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Integer totalHours,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Integer totalMinutes) {

    @Builder
    public Activity {
    }
}
