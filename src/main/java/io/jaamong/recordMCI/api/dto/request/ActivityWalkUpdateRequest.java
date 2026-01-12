package io.jaamong.recordMCI.api.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

public record ActivityWalkUpdateRequest(
        @Min(value = 0, message = "총 걸음수는 0부터 입력할 수 있습니다.")
        Integer totalSteps,
        @Range(min = 0, max = 23, message = "총 시간(hour)은 최소 0, 최대 23까지 입력할 수 있습니다.")
        Integer totalHours,
        @Range(min = 0, max = 59, message = "총 분(minute)은 최소 0, 최대 59까지 입력할 수 있습니다.")
        Integer totalMinutes
) {

    @Builder
    public ActivityWalkUpdateRequest {
    }

    public ActivityWalkUpdateServiceRequest toServiceRequest(Long id) {
        return ActivityWalkUpdateServiceRequest.builder()
                .id(id)
                .totalSteps(totalSteps)
                .totalHours(totalHours)
                .totalMinutes(totalMinutes)
                .build();
    }
}
