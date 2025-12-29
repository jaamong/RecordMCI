package io.jaamong.recordMCI.domain.repository;

import io.jaamong.recordMCI.domain.entity.FoodEntity;
import io.jaamong.recordMCI.infrastructure.FoodJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FoodRepository {

    private final FoodJpaRepository foodJpaRepository;

    public List<FoodEntity> saveAll(List<FoodEntity> foodEntities) {
        return foodJpaRepository.saveAll(foodEntities);
    }
}
