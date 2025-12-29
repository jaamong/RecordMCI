package io.jaamong.recordMCI.domain.application;

import io.jaamong.recordMCI.domain.dto.Activity;
import io.jaamong.recordMCI.domain.entity.ActivityEntity;
import io.jaamong.recordMCI.domain.entity.ActivityType;
import io.jaamong.recordMCI.domain.entity.DailyRecordEntity;
import io.jaamong.recordMCI.domain.repository.ActivityRepository;
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

    /**
     * DailyRecord가 오늘 처음 생성될 때 같이 생성되는 디폴트 activity
     * - walk : 걷기(산책)
     * - bible transcribe : 성경 필사
     * - coloring book : 색칠 공부
     * @return Activity 리스트
     */
    @Transactional
    public List<ActivityEntity> initCreate(DailyRecordEntity dailyRecordEntity) {
        ActivityEntity walk = ActivityEntity.of(dailyRecordEntity, ActivityType.WALK);
        ActivityEntity bibleTranscribe = ActivityEntity.of(dailyRecordEntity, ActivityType.BIBLE_TRANSCRIBE);
        ActivityEntity coloringBook = ActivityEntity.of(dailyRecordEntity, ActivityType.COLORING_BOOK);

        List<ActivityEntity> activities = new ArrayList<>();
        activities.add(walk);
        activities.add(bibleTranscribe);
        activities.add(coloringBook);

        activities = activityRepository.saveAll(activities);

        return activities;
    }

    @Transactional
    public Activity invertCompleted(Long id) {
        ActivityEntity activityEntity = activityRepository.findById(id);
        activityEntity.invertCompleted();
        return activityEntity.toModel();
    }
}