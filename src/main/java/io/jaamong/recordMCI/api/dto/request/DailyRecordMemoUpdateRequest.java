package io.jaamong.recordMCI.api.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;

public record DailyRecordMemoUpdateRequest(
        @Size(max = 1000, message = "메모는 최대 1,000자까지 허용됩니다.")
        String memo
) {
    @Builder
    public DailyRecordMemoUpdateRequest {
    }

    public DailyRecordMemoUpdateServiceRequest toServiceRequest(Long id) {
        return DailyRecordMemoUpdateServiceRequest.builder()
                .id(id)
                .memo(this.memo)
                .build();
    }
}
