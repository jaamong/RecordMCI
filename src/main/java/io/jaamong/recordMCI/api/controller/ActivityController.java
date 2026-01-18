package io.jaamong.recordMCI.api.controller;

import io.jaamong.recordMCI.api.ApiResponse;
import io.jaamong.recordMCI.api.dto.request.activity.ActivitySaveRequest;
import io.jaamong.recordMCI.api.dto.request.activity.ActivityWalkUpdateRequest;
import io.jaamong.recordMCI.api.dto.request.activity.ActivityNameUpdateRequest;
import io.jaamong.recordMCI.domain.application.ActivityService;
import io.jaamong.recordMCI.domain.dto.Activity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Activity.WALK 상태가 true인 경우 호출되는 WALK 상세정보 수정 API
     *
     * @param `ActivityEntity` DB index
     * @return Activity 객체와 성공 응답
     */
    @PutMapping("/{id}/walk-detail")
    public ApiResponse<Activity> registerWalkDetail(@PathVariable("id") Long id,
                                                    @RequestBody @Valid ActivityWalkUpdateRequest request) {
        Activity activity = activityService.updateWalkDetail(request.toServiceRequest(id));
        return ApiResponse.ok(activity);
    }

    /**
     * 단일 활동 아이템의 이름 수정
     *
     * @param `ActivityEntity` DB index, 변경 후 이름 (name after updating)
     * @return Activity 객체와 성공 응답
     */
    @PutMapping("/{id}/name")
    public ApiResponse<Activity> rename(@PathVariable("id") Long id,
                                        @RequestBody @Valid ActivityNameUpdateRequest request) {
        Activity activity = activityService.updateName(request.toServiceRequest(id));
        return ApiResponse.ok(activity);
    }

    /**
     * 새로운 Activity 추가
     *
     * @param `DailyRecordEntity` DB index, 활동 이름
     * @return Activity 객체와 성공 응답
     */
    @PostMapping
    public ApiResponse<Activity> add(@RequestBody @Valid ActivitySaveRequest request) {
        Activity activity = activityService.create(request.toServiceRequest());
        return ApiResponse.ok(activity);
    }
}
