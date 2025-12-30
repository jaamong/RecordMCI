package io.jaamong.recordMCI.domain.application;

import io.jaamong.recordMCI.api.dto.response.DailyRecordGetOneResponse;
import io.jaamong.recordMCI.domain.dto.Activity;
import io.jaamong.recordMCI.domain.dto.Food;
import io.jaamong.recordMCI.domain.entity.*;
import io.jaamong.recordMCI.domain.repository.DailyRecordRepository;
import io.jaamong.recordMCI.infrastructure.ActivityJpaRepository;
import io.jaamong.recordMCI.infrastructure.FoodJpaRepository;
import io.jaamong.recordMCI.infrastructure.UsersJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class DailyRecordServiceTest {

    @Autowired
    DailyRecordService dailyRecordService;

    @Autowired
    UsersJpaRepository usersJpaRepository;

    @Autowired
    DailyRecordRepository dailyRecordRepository;

    @Autowired
    ActivityJpaRepository activityJpaRepository;

    @Autowired
    FoodJpaRepository foodJpaRepository;

//    @AfterEach
//    void tearDown() {
//       // 연관관계 jpaRepository 까지 해제해야해서 이방법은 취소
//        dailyRecordJpaRepository.deleteAllInBatch();
//        usersJpaRepository.deleteAllInBatch();
//    }


    @DisplayName("오늘의 DailyRecord를 단일 상세 조회할 수 있으며, 첫 접속이므로 지금 초기화된다.")
    @Test
    void getTodayBy() {
        // given
        UserEntity user = createUser();

        // when
        DailyRecordGetOneResponse response = dailyRecordService.getTodayBy(user.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.memo()).isNull();

        assertThat(response.foods()).hasSize(5);
        assertThat(response.foods()).extracting(Food::name)
                .containsExactlyInAnyOrder("콩류", "브로콜리", "견과류", "요거트", "블루베리");

        assertThat(response.activities()).hasSize(3);
        assertThat(response.activities()).extracting(Activity::activityType)
                .containsExactlyInAnyOrder(
                        ActivityType.WALK,
                        ActivityType.BIBLE_TRANSCRIBE,
                        ActivityType.COLORING_BOOK
                );
    }

    @DisplayName("특정 날짜의 DailyRecord를 상세 조회할 수 있다.")
    @Test
    void getDayBy() {
        // given
        LocalDate targetDate = LocalDate.now().plusDays(1);
        UserEntity user = createUser();
        createDailyRecord(targetDate, user);

        // when
        DailyRecordGetOneResponse response = dailyRecordService.getDayBy(user.getId(), targetDate);

        // then
        assertThat(response).isNotNull();
        assertThat(response.foods()).hasSize(1);
        assertThat(response.activities()).hasSize(1);
    }

    private UserEntity createUser() {
        UserEntity user = new UserEntity("test1", "password1");
        return usersJpaRepository.save(user);
    }

    private void createDailyRecord(LocalDate targetDate, UserEntity user) {
        DailyRecordEntity dailyRecordEntity = DailyRecordEntity.builder()
                .date(targetDate)
                .userEntity(user)
                .build();

        dailyRecordRepository.save(dailyRecordEntity);

        ActivityEntity activityEntity = ActivityEntity.of(dailyRecordEntity, ActivityType.WALK);
        activityEntity = activityJpaRepository.save(activityEntity);
        dailyRecordEntity.addActivity(activityEntity);

        FoodEntity foodEntity = FoodEntity.of(dailyRecordEntity, NutrientType.PROTEIN, "test");
        foodEntity = foodJpaRepository.save(foodEntity);
        dailyRecordEntity.addFood(foodEntity);
    }

}