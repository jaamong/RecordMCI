package io.jaamong.recordMCI.api.controller;

import io.jaamong.recordMCI.api.ApiResponse;
import io.jaamong.recordMCI.api.dto.response.DailyRecordGetOneResponse;
import io.jaamong.recordMCI.domain.application.DailyRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
    @GetMapping("/{userId}")
    public ApiResponse<DailyRecordGetOneResponse> today(@PathVariable("userId") Long userId,
                                                        @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date != null) {
            // 특정 날짜 조회
            return ApiResponse.ok(dailyRecordService.getDayBy(userId, date));
        }
        // 금일 조회
        return ApiResponse.ok(dailyRecordService.getTodayBy(userId));
    }

}
