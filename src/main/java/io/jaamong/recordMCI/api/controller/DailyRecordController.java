package io.jaamong.recordMCI.api.controller;

import io.jaamong.recordMCI.api.ApiResponse;
import io.jaamong.recordMCI.api.dto.response.DailyRecordGetOneResponse;
import io.jaamong.recordMCI.domain.application.DailyRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/daily-records")
@RequiredArgsConstructor
public class DailyRecordController {

    private final DailyRecordService dailyRecordService;

    /**
     * today DailyRecord 상세 조회
     * @param userId
     * @return DailyRecord 상세 정보
     */
    @GetMapping("/{userId}")
    public ApiResponse<DailyRecordGetOneResponse> today(@PathVariable("userId") Long userId) {
        DailyRecordGetOneResponse response = dailyRecordService.getTodayBy(userId);
        return ApiResponse.ok(response);
    }

    // TODO: 날짜별 상세 조회
    @GetMapping("/{userId}/{date}")
    public ApiResponse<Void> one(@PathVariable("userId") Long userId, @PathVariable("date") String date) {
        return null;
    }
}
