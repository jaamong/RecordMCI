package io.jaamong.recordMCI.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

public record Activity(
        Long id,
        Long dailyRecordId,
        String name,
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
