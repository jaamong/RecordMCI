package io.jaamong.recordMCI.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 4xx
    INVALID_ACTIVITY_WALK_UPDATE_REQUEST(HttpStatus.BAD_REQUEST, "걷기(Activity.WALK) 상세 정보를 수정하려면, 걷기 상태가 true 상태여야 합니다."),
    INVALID_NUTRIENT_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 영양 성분(NutrientType)입니다."),

    NOT_FOUND_RECORD(HttpStatus.NOT_FOUND, "기록(DailyRecord)를 찾을 수 없습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_FOOD(HttpStatus.NOT_FOUND, "식품(FoodEntity)을 찾을 수 없습니다."),
    NOT_FOUND_ACTIVITY(HttpStatus.NOT_FOUND, "활동(ActivityEntity)를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
