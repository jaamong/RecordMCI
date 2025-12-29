package io.jaamong.recordMCI.domain.application;

import io.jaamong.recordMCI.api.dto.response.DailyRecordGetOneResponse;
import io.jaamong.recordMCI.domain.dto.Activity;
import io.jaamong.recordMCI.domain.dto.Food;
import io.jaamong.recordMCI.domain.entity.ActivityType;
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
class DailyRecordServiceTest {

    @Autowired
    DailyRecordService dailyRecordService;

    @Autowired
    UsersJpaRepository usersJpaRepository;

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

    private UserEntity createUser() {
        UserEntity user = new UserEntity("test1", "password1");
        return usersJpaRepository.save(user);
    }

}