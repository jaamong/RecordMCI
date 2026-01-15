package io.jaamong.recordMCI.domain.repository;

import io.jaamong.recordMCI.api.exception.CustomRuntimeException;
import io.jaamong.recordMCI.api.exception.ErrorCode;
import io.jaamong.recordMCI.domain.dto.Food;
import io.jaamong.recordMCI.domain.entity.FoodEntity;
import io.jaamong.recordMCI.infrastructure.FoodJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FoodRepository {

    private final FoodJpaRepository foodJpaRepository;

    public FoodEntity save(FoodEntity foodEntity) {
        return foodJpaRepository.save(foodEntity);
    }

    public List<FoodEntity> saveAll(List<FoodEntity> foodEntities) {
        return foodJpaRepository.saveAll(foodEntities);
    }

    public Food getById(Long id) {
        return findById(id).toModel();
    }

    public FoodEntity findById(Long id) {
        return foodJpaRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_FOOD));
    }

    public void deleteById(Long id) {
        foodJpaRepository.deleteById(id);
    }
}
