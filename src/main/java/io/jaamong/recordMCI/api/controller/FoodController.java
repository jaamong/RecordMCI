package io.jaamong.recordMCI.api.controller;

import io.jaamong.recordMCI.api.ApiResponse;
import io.jaamong.recordMCI.api.dto.request.food.FoodNameUpdateRequest;
import io.jaamong.recordMCI.api.dto.request.food.FoodSaveRequest;
import io.jaamong.recordMCI.domain.application.FoodService;
import io.jaamong.recordMCI.domain.dto.Food;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 단일 식품 아이템의 이름 수정
     *
     * @param id
     * @param request
     * @return name 필드가 변경된 Food 객체와 성공 응답
     */
    @PutMapping("/{id}/name")
    public ApiResponse<Food> rename(@PathVariable("id") Long id,
                                    @Valid @RequestBody FoodNameUpdateRequest request) {
        Food food = foodService.updateName(request.toServiceRequest(id));
        return ApiResponse.ok(food);
    }

    /**
     * 새로운 식품 추가
     *
     * @param request
     * @return
     */
    @PostMapping
    public ApiResponse<Food> add(@Valid @RequestBody FoodSaveRequest request) {
        Food food = foodService.create(request.toServiceRequest());
        return ApiResponse.ok(food);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        foodService.delete(id);
        return ApiResponse.of(HttpStatus.NO_CONTENT, null);
    }
}
