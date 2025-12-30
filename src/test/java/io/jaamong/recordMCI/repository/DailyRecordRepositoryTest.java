package io.jaamong.recordMCI.repository;

import io.jaamong.recordMCI.api.exception.CustomRuntimeException;
import io.jaamong.recordMCI.api.exception.ErrorCode;
import io.jaamong.recordMCI.domain.dto.DailyRecord;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class DailyRecordRepositoryTest {

    @Autowired
    DailyRecordRepository dailyRecordRepository;

    @Autowired
    ActivityJpaRepository activityJpaRepository;

    @Autowired
    FoodJpaRepository foodJpaRepository;

    @Autowired
    UsersJpaRepository usersJpaRepository;


    @DisplayName("오늘 생성된 객체를 조회한다.")
    @Test
    void findTodayByUserId() {
        // given
        LocalDate now = LocalDate.now();

        UserEntity user = createUser();
        Long userId = user.getId();

        createDailyRecord(now, user);

        // when
        Optional<DailyRecord> result = dailyRecordRepository.findTodayByUserId(userId);

        // then
        assertThat(result.isPresent()).isTrue();
        DailyRecord dailyRecord = result.get();
        assertThat(dailyRecord.user()).isNotNull();
        assertThat(dailyRecord.date()).isEqualTo(now);
    }

    @DisplayName("오늘이 아닌 특정 날짜의 DailyRecord 객체를 조회한다.")
    @Test
    void getDayByUserIdAndDate() {
        // given
        LocalDate targetDate = LocalDate.now().plusDays(1);
        UserEntity user = createUser();
        createDailyRecord(targetDate, user);

        // when
        DailyRecord result = dailyRecordRepository.getDayByUserIdAndDate(user.getId(), targetDate);

        // then
        assertThat(result).isNotNull();
        assertThat(result.date()).isEqualTo(targetDate);
    }

    @DisplayName("특정 날짜의 DailyRecord 객체 조회를 실패하면 NOT FOUND 예외가 발생한다.")
    @Test
    void getDayByUserIdAndDate_onFail() {
        // given
        LocalDate targetDate = LocalDate.now().plusDays(1);
        UserEntity user = createUser();

        // when, then
        assertThatThrownBy(() ->
                dailyRecordRepository.getDayByUserIdAndDate(user.getId(), targetDate))
                .isInstanceOf(CustomRuntimeException.class)
                .hasMessage(ErrorCode.NOT_FOUND_RECORD.getMessage());
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