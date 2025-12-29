package io.jaamong.recordMCI.domain.repository;

import io.jaamong.recordMCI.domain.dto.DailyRecord;
import io.jaamong.recordMCI.domain.entity.DailyRecordEntity;
import io.jaamong.recordMCI.infrastructure.DailyRecordJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime startOfTmr = today.plusDays(1).atStartOfDay();
        return dailyRecordJpaRepository.findTodayByUserId(userId, startOfDay, startOfTmr)
                .map(DailyRecordEntity::toModel);
    }
}
