package io.jaamong.recordMCI.domain.application;

import io.jaamong.recordMCI.domain.dto.Activity;
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
class ActivityServiceTest {

    @Autowired
    DailyRecordService dailyRecordService;

    @Autowired
    ActivityService activityService;

    @Autowired
    UsersJpaRepository usersJpaRepository;


    @DisplayName("ActivityEntity의 completed 필드를 반전(invert)시킬 수 있다: 초기화된 DailyRecord가 가진 Activity 중 하나를 true로 만든다.")
    @Test
    void invertCompleted() {
        // given
        UserEntity userEntity = createUser();
        var todayDailyRecord = dailyRecordService.getTodayBy(userEntity.getId());

        // when
        Activity activity = todayDailyRecord.activities().get(1);
        Activity completedActivity = activityService.invertCompleted(activity.id());

        // then
        Assertions.assertThat(completedActivity.completed()).isTrue();
    }

    private UserEntity createUser() {
        UserEntity user = new UserEntity("test1", "password1");
        return usersJpaRepository.save(user);
    }
}