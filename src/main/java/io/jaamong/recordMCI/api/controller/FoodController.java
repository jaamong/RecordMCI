package io.jaamong.recordMCI.api.controller;

import io.jaamong.recordMCI.api.ApiResponse;
import io.jaamong.recordMCI.domain.application.FoodService;
import io.jaamong.recordMCI.domain.dto.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    /**
     * UI 상에서 단일 식품 아이템 체크박스 클릭 시 FoodEntity.consumed 상태 변경
     *
     * @param `FoodEntity` DB index
     * @return consumed 필드가 반전된(inverted) Food 객체와 성공 응답
     */
    @PutMapping("/{id}")
    public ApiResponse<Food> consume(@PathVariable("id") Long id) {
        Food food = foodService.invertConsumed(id);
        return ApiResponse.ok(food);
    }
}
