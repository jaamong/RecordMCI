package io.jaamong.recordMCI.domain.application;

import io.jaamong.recordMCI.api.dto.request.ActivityWalkUpdateServiceRequest;
import io.jaamong.recordMCI.api.dto.response.DailyRecordGetDetailResponse;
import io.jaamong.recordMCI.api.exception.CustomRuntimeException;
import io.jaamong.recordMCI.api.exception.ErrorCode;
import io.jaamong.recordMCI.domain.dto.Activity;
import io.jaamong.recordMCI.domain.entity.ActivityType;
import io.jaamong.recordMCI.domain.entity.UserEntity;
import io.jaamong.recordMCI.infrastructure.UsersJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThat(completedActivity.completed()).isTrue();
    }

    @DisplayName("ActivityEntity.Walk.completed가 true면 walk의 상세 정보를 수정할 수 있다.")
    @Test
    void updateWalkDetail() {
        // given
        UserEntity userEntity = createUser();
        var dailyRecord = dailyRecordService.getTodayBy(userEntity.getId());

        Long completedWalkId = getWalkIdBy(dailyRecord);

        Activity completedWalk = activityService.invertCompleted(completedWalkId);

        int totalSteps = 1000;
        int totalHours = 0;
        int totalMinutes = 10;
        var request = createActivityWalkUpdateServiceRequest(completedWalkId, totalSteps, totalHours, totalMinutes);

        // when
        completedWalk = activityService.updateWalkDetail(request);

        // then
        assertThat(completedWalk.completed()).isTrue();
        assertThat(completedWalk.activityType()).isEqualTo(ActivityType.WALK);
        assertThat(completedWalk.totalSteps()).isEqualTo(totalSteps);
        assertThat(completedWalk.totalHours()).isEqualTo(totalHours);
        assertThat(completedWalk.totalMinutes()).isEqualTo(totalMinutes);
    }

    @DisplayName("ActivityEntity.Walk.completed가 false면 예외가 발생하고 수정에 실패한다.")
    @Test
    void updateWalkDetail_onFail() {
        // given
        UserEntity userEntity = createUser();
        var dailyRecord = dailyRecordService.getTodayBy(userEntity.getId());

        Long uncompletedWalkId = getWalkIdBy(dailyRecord);
        var request = createActivityWalkUpdateServiceRequest(uncompletedWalkId, 0,0,0);

        // when, then
        assertThatThrownBy(() -> activityService.updateWalkDetail(request))
                .isInstanceOf(CustomRuntimeException.class)
                .hasMessage(ErrorCode.INVALID_ACTIVITY_WALK_UPDATE_REQUEST.getMessage());
    }

    private UserEntity createUser() {
        UserEntity user = new UserEntity("test1", "password1");
        return usersJpaRepository.save(user);
    }

    private Long getWalkIdBy(DailyRecordGetDetailResponse dailyRecord) {
        Optional<Activity> optionalWalk = dailyRecord.activities().stream()
                .filter(activity -> activity.activityType().equals(ActivityType.WALK))
                .findFirst();
        Activity walk = optionalWalk.get();
        return walk.id();
    }

    private ActivityWalkUpdateServiceRequest createActivityWalkUpdateServiceRequest(Long completedWalkId, int totalSteps, int totalHours, int totalMinutes) {
        return new ActivityWalkUpdateServiceRequest(completedWalkId, totalSteps, totalHours, totalMinutes);
    }
}