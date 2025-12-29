package io.jaamong.recordMCI.api.controller;

import io.jaamong.recordMCI.api.ApiResponse;
import io.jaamong.recordMCI.domain.application.ActivityService;
import io.jaamong.recordMCI.domain.dto.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    /**
     * UI 상에서 단일 활동 아이템 체크박스 클릭 시 ActivityEntity.consumed 상태 변경
     *
     * @param `ActivityEntity` DB index
     * @return completed 필드가 반전된(inverted) Activity 객체와 성공 응답
     */
    @PutMapping("/{id}")
    public ApiResponse<Activity> complete(@PathVariable("id") Long id) {
        Activity activity = activityService.invertCompleted(id);
        return ApiResponse.ok(activity);
    }
}
