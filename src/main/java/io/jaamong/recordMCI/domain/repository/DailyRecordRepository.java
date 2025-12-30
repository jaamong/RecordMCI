package io.jaamong.recordMCI.domain.repository;

import io.jaamong.recordMCI.api.exception.CustomRuntimeException;
import io.jaamong.recordMCI.api.exception.ErrorCode;
import io.jaamong.recordMCI.domain.dto.DailyRecord;
import io.jaamong.recordMCI.domain.entity.DailyRecordEntity;
import io.jaamong.recordMCI.infrastructure.DailyRecordJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DailyRecordRepository {

    private final DailyRecordJpaRepository dailyRecordJpaRepository;

    public DailyRecordEntity save(DailyRecordEntity dailyRecordEntity) {
        return dailyRecordJpaRepository.save(dailyRecordEntity);
    }

    public Optional<DailyRecord> findTodayByUserId(Long userId) {
        LocalDate today = LocalDate.now();
        return findDayBy(userId, today);
    }

    public DailyRecord getDayByUserIdAndDate(Long userId, LocalDate date) {
        return findDayBy(userId, date).orElseThrow(
                () -> new CustomRuntimeException(ErrorCode.NOT_FOUND_RECORD));
    }

    public Optional<DailyRecord> findDayBy(Long userId, LocalDate targetDate) {
        return dailyRecordJpaRepository.findDateByUserId(userId, targetDate)
                .map(DailyRecordEntity::toModel);
    }

}
