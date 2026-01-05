package io.jaamong.recordMCI.api.controller;

import io.jaamong.recordMCI.api.ApiResponse;
import io.jaamong.recordMCI.api.dto.request.DailyRecordMemoUpdateRequest;
import io.jaamong.recordMCI.api.dto.response.DailyRecordGetDetailResponse;
import io.jaamong.recordMCI.api.dto.response.DailyRecordGetMonthResponse;
import io.jaamong.recordMCI.domain.application.DailyRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/daily-records")
@RequiredArgsConstructor
public class DailyRecordController {

    private final DailyRecordService dailyRecordService;

    /**
     * 금일 또는 특정 날짜 DailyRecord 상세 조회
     *
     * @param userId
     * @param date   - required = false
     *               - false인 경우 today 조회
     *               - true인 경우 특정 날짜 조회
     * @return DailyRecord 상세 정보
     */
    @GetMapping("/{id}")
    public ApiResponse<DailyRecordGetDetailResponse> today(@PathVariable("id") Long userId,
                                                           @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date != null) {
            // 특정 날짜 조회
            return ApiResponse.ok(dailyRecordService.getDayBy(userId, date));
        }
        // 금일 조회
        return ApiResponse.ok(dailyRecordService.getTodayBy(userId));
    }

    /**
     * 먼슬리 조회 API
     *
     * @param userId
     * @param year
     * @param month
     * @return 한달 치 간략 DailyRecord 정보
     * - 식품, 활동, 메모 true 여부만 반환
     */
    @GetMapping("/{id}/monthly")
    public ApiResponse<List<DailyRecordGetMonthResponse>> monthly(@PathVariable("id") Long userId,
                                                                  @RequestParam(value = "year") Integer year,
                                                                  @RequestParam(value = "month") Integer month) {
        var response = dailyRecordService.getMonthlyBy(userId, year, month);
        return ApiResponse.ok(response);
    }

    @PutMapping("/{id}/memo")
    public ApiResponse<String> writeMemo(@PathVariable("id") Long id,
                                         @Valid @RequestBody DailyRecordMemoUpdateRequest request) {
        var response = dailyRecordService.patchMemo(request.toServiceRequest(id));
        return ApiResponse.ok(response);
    }
}
