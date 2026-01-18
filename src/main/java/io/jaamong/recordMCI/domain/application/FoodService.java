package io.jaamong.recordMCI.domain.application;

import io.jaamong.recordMCI.api.dto.request.food.FoodNameUpdateServiceRequest;
import io.jaamong.recordMCI.api.dto.request.food.FoodSaveServiceRequest;
import io.jaamong.recordMCI.domain.dto.Food;
import io.jaamong.recordMCI.domain.entity.DailyRecordEntity;
import io.jaamong.recordMCI.domain.entity.FoodEntity;
import io.jaamong.recordMCI.domain.entity.NutrientType;
import io.jaamong.recordMCI.domain.repository.DailyRecordRepository;
import io.jaamong.recordMCI.domain.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final DailyRecordRepository dailyRecordRepository;

    /**
     * DailyRecord가 오늘 처음 생성될 때 같이 생성되는 디폴트 food
     * - beans : 콩류
     * - broccoli : 브로콜리
     * - nuts : 견과류
     * - yogurt : 요거트
     * - blueberry : 블루베리
     *
     * @return Food 리스트
     */
    public List<FoodEntity> initCreate(DailyRecordEntity dailyRecordEntity) {
        FoodEntity beans = FoodEntity.of(dailyRecordEntity, NutrientType.PROTEIN, "콩류");
        FoodEntity broccoli = FoodEntity.of(dailyRecordEntity, NutrientType.CARBO_VEGETABLE, "브로콜리");
        FoodEntity nuts = FoodEntity.of(dailyRecordEntity, NutrientType.FAT, "견과류");
        FoodEntity yogurt = FoodEntity.of(dailyRecordEntity, NutrientType.PROTEIN, "요거트");
        FoodEntity blueberry = FoodEntity.of(dailyRecordEntity, NutrientType.CARBO_FRUIT, "블루베리");

        List<FoodEntity> foodEntities = new ArrayList<>();
        foodEntities.add(beans);
        foodEntities.add(broccoli);
        foodEntities.add(nuts);
        foodEntities.add(yogurt);
        foodEntities.add(blueberry);

        foodEntities = foodRepository.saveAll(foodEntities);

        return foodEntities;
    }

    /**
     * 새로운 Food 추가
     * - consumed 필드는 false로 초기화한다.
     *
     * @param request
     * @return
     */
    public Food create(FoodSaveServiceRequest request) {
        log.info("[FoodService] create :: start : request={}", request);

        DailyRecordEntity dailyRecordEntity = dailyRecordRepository.getDayById(request.dailyRecordId());
        FoodEntity foodEntity = FoodEntity.of(dailyRecordEntity, request.nutrientType(), request.name());
        Food food = foodRepository.save(foodEntity).toModel();

        log.info("[FoodService] create :: finish : food={}", food.toString());
        return food;
    }

    public Food invertConsumed(Long id) {
        FoodEntity foodEntity = foodRepository.findById(id);
        foodEntity.invertConsumed();
        return foodEntity.toModel();
    }

    public Food updateName(FoodNameUpdateServiceRequest request) {
        FoodEntity foodEntity = foodRepository.findById(request.id());
        foodEntity.updateName(request.name());
        return foodEntity.toModel();
    }

    public void delete(Long id) {
        foodRepository.deleteById(id);
    }
}