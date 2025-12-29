package io.jaamong.recordMCI.repository;

import io.jaamong.recordMCI.domain.dto.DailyRecord;
import io.jaamong.recordMCI.domain.entity.*;
import io.jaamong.recordMCI.domain.repository.DailyRecordRepository;
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

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class DailyRecordRepositoryTest {

    @Autowired
    DailyRecordRepository dailyRecordRepository;

    @Autowired
    UsersJpaRepository usersJpaRepository;


    @DisplayName("오늘 생성된 객체를 조회한다.")
    @Test
    void findTodayByUserId() {
        // given
        LocalDate now = LocalDate.now();

        UserEntity user = createUser();
        Long userId = user.getId();

        DailyRecordEntity dailyRecordEntity = DailyRecordEntity.builder()
                .date(now)
                .userEntity(user)
                .build();

        dailyRecordRepository.save(dailyRecordEntity);

        dailyRecordEntity.addActivity(
                ActivityEntity.of(dailyRecordEntity, ActivityType.WALK)
        );
        dailyRecordEntity.addFood(
                FoodEntity.of(dailyRecordEntity, NutrientType.PROTEIN, "test")
        );

        // when
        Optional<DailyRecord> result = dailyRecordRepository.findTodayByUserId(userId);

        // then
        assertThat(result.isPresent()).isTrue();
        DailyRecord dailyRecord = result.get();
        assertThat(dailyRecord.user()).isNotNull();
        assertThat(dailyRecord.date()).isEqualTo(now);
    }

    private UserEntity createUser() {
        UserEntity user = new UserEntity("test1", "password1");
        return usersJpaRepository.save(user);
    }
}