package io.jaamong.recordMCI.domain.application;

import io.jaamong.recordMCI.api.dto.request.activity.ActivitySaveServiceRequest;
import io.jaamong.recordMCI.api.dto.request.activity.ActivityWalkUpdateServiceRequest;
import io.jaamong.recordMCI.domain.dto.Activity;
import io.jaamong.recordMCI.domain.entity.ActivityEntity;
import io.jaamong.recordMCI.domain.entity.ActivityInitialType;
import io.jaamong.recordMCI.domain.entity.DailyRecordEntity;
import io.jaamong.recordMCI.domain.repository.ActivityRepository;
import io.jaamong.recordMCI.domain.repository.DailyRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final DailyRecordRepository dailyRecordRepository;

    /**
     * DailyRecord가 오늘 처음 생성될 때 같이 생성되는 디폴트 activity
     * - walk : 걷기(산책)
     * - bible transcribe : 성경 필사
     * - coloring book : 색칠 공부
     *
     * @return Activity 리스트
     */
    @Transactional
    public List<ActivityEntity> initCreate(DailyRecordEntity dailyRecordEntity) {
        ActivityEntity walk = ActivityEntity.of(dailyRecordEntity, ActivityInitialType.WALK.getType());
        ActivityEntity bibleTranscribe = ActivityEntity.of(dailyRecordEntity, ActivityInitialType.BIBLE_TRANSCRIBE.getType());
        ActivityEntity coloringBook = ActivityEntity.of(dailyRecordEntity, ActivityInitialType.COLORING_BOOK.getType());

        List<ActivityEntity> activities = new ArrayList<>();
        activities.add(walk);
        activities.add(bibleTranscribe);
        activities.add(coloringBook);

        activities = activityRepository.saveAll(activities);

        return activities;
    }

    @Transactional
    public Activity create(ActivitySaveServiceRequest request) {
        log.info("[ActivityService] create :: start : request={}", request);

        DailyRecordEntity dailyRecordEntity = dailyRecordRepository.getDayById(request.dailyRecordId());
        ActivityEntity activityEntity = ActivityEntity.of(dailyRecordEntity, request.name());
        Activity activity = activityRepository.save(activityEntity).toModel();

        log.info("[ActivityService] create :: finish : activity={}", activity);
        return activity;
    }

    @Transactional
    public Activity invertCompleted(Long id) {
        ActivityEntity activityEntity = activityRepository.findById(id);
        activityEntity.invertCompleted();
        return activityEntity.toModel();
    }

    @Transactional
    public Activity updateWalkDetail(ActivityWalkUpdateServiceRequest request) {
        ActivityEntity activityEntity = activityRepository.findById(request.id());
        activityEntity.updateWalk(
                request.totalSteps(),
                request.totalHours(),
                request.totalMinutes()
        );
        return activityEntity.toModel();
    }
}