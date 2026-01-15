package io.jaamong.recordMCI.domain.application;

import io.jaamong.recordMCI.api.dto.request.food.FoodNameUpdateServiceRequest;
import io.jaamong.recordMCI.api.dto.request.food.FoodSaveRequest;
import io.jaamong.recordMCI.domain.dto.Food;
import io.jaamong.recordMCI.domain.entity.NutrientType;
import io.jaamong.recordMCI.domain.entity.UserEntity;
import io.jaamong.recordMCI.infrastructure.UsersJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class FoodServiceTest {

    @Autowired
    FoodService foodService;

    @Autowired
    UsersJpaRepository usersJpaRepository;

    @Autowired
    DailyRecordService dailyRecordService;


    @DisplayName("새로운 식품을 생성할 수 있다.")
    @Test
    void create() {
        // given
        UserEntity userEntity = createUser();
        var recordGetDetailResponse = dailyRecordService.getTodayBy(userEntity.getId());

        String name = "food test 1";
        NutrientType nutrientType = NutrientType.PROTEIN;
        FoodSaveRequest request = FoodSaveRequest.builder()
                .dailyRecordId(recordGetDetailResponse.id())
                .name(name)
                .nutrientType(nutrientType.getType())
                .build();

        // when
        Food result = foodService.create(request.toServiceRequest());

        // then
        assertThat(result).isNotNull();
        assertThat(result)
                .extracting("name", "consumed", "nutrientType")
                .containsExactlyInAnyOrder(name, false, nutrientType);
    }

    @DisplayName("FoodEntity의 consumed 필드를 반전(invert)시킬 수 있다: 초기화된 DailyRecord가 가진 Food 중 하나를 true로 만든다.")
    @Test
    void invertConsumed() {
        // given
        UserEntity userEntity = createUser();
        var todayDailyRecord = dailyRecordService.getTodayBy(userEntity.getId());

        // when
        Food food = todayDailyRecord.foods().get(1);
        Food consumedFood = foodService.invertConsumed(food.id());

        // then
        assertThat(consumedFood.consumed()).isTrue();
    }

    @DisplayName("FoodEntity의 name 필드를 변경할 수 있다.")
    @Test
    void updateName() {
        // given
        UserEntity userEntity = createUser();
        var todayDailyRecord = dailyRecordService.getTodayBy(userEntity.getId());

        Food food = todayDailyRecord.foods().get(1);
        var request = new FoodNameUpdateServiceRequest(food.id(), "update");

        // when
        Food updatedFood = foodService.updateName(request);

        // then
        assertThat(updatedFood.name()).isEqualTo(request.name());
    }

    @DisplayName("FoodEntity를 삭제할 수 있다.")
    @Test
    void delete() {
        // given
        UserEntity userEntity = createUser();
        var todayDailyRecord = dailyRecordService.getTodayBy(userEntity.getId());
        int initFoodsSize = todayDailyRecord.foods().size();

        Food food = todayDailyRecord.foods().get(1);

        // when
        foodService.delete(food.id());

        // then
        var result = dailyRecordService.getTodayBy(userEntity.getId());
        assertThat(result.foods().size()).isEqualTo(initFoodsSize - 1);
    }

    private UserEntity createUser() {
        UserEntity user = new UserEntity("test1", "password1");
        return usersJpaRepository.save(user);
    }

}