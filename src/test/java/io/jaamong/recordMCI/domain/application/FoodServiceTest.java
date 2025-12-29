package io.jaamong.recordMCI.domain.application;

import io.jaamong.recordMCI.domain.dto.Food;
import io.jaamong.recordMCI.domain.entity.UserEntity;
import io.jaamong.recordMCI.infrastructure.UsersJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class FoodServiceTest {

    @Autowired
    FoodService foodService;

    @Autowired
    UsersJpaRepository usersJpaRepository;

    @Autowired
    DailyRecordService dailyRecordService;


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
        Assertions.assertThat(consumedFood.consumed()).isTrue();
    }

    private UserEntity createUser() {
        UserEntity user = new UserEntity("test1", "password1");
        return usersJpaRepository.save(user);
    }

}