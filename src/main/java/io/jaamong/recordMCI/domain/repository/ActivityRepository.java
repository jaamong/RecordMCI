package io.jaamong.recordMCI.domain.repository;

import io.jaamong.recordMCI.domain.entity.ActivityEntity;
import io.jaamong.recordMCI.infrastructure.ActivityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActivityRepository {

    private final ActivityJpaRepository activityJpaRepository;

    public void save(ActivityEntity activityEntity) {
        activityJpaRepository.save(activityEntity);
    }

    public List<ActivityEntity> saveAll(List<ActivityEntity> activities) {
        return activityJpaRepository.saveAll(activities);
    }
}
