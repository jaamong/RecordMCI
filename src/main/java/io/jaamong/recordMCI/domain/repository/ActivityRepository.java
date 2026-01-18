package io.jaamong.recordMCI.domain.repository;

import io.jaamong.recordMCI.api.exception.CustomRuntimeException;
import io.jaamong.recordMCI.api.exception.ErrorCode;
import io.jaamong.recordMCI.domain.entity.ActivityEntity;
import io.jaamong.recordMCI.infrastructure.ActivityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActivityRepository {

    private final ActivityJpaRepository activityJpaRepository;

    public ActivityEntity save(ActivityEntity activityEntity) {
        return activityJpaRepository.save(activityEntity);
    }

    public List<ActivityEntity> saveAll(List<ActivityEntity> activities) {
        return activityJpaRepository.saveAll(activities);
    }

    public ActivityEntity findById(Long id) {
        return activityJpaRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_ACTIVITY));
    }

    public void deleteById(Long id) {
        activityJpaRepository.deleteById(id);
    }
}
