package io.jaamong.recordMCI.infrastructure;

import io.jaamong.recordMCI.domain.entity.DailyRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DailyRecordJpaRepository extends JpaRepository<DailyRecordEntity, Long> {

    @Query("select dre from DailyRecordEntity dre " +
            "where dre.userEntity.id = :userId AND " +
            "dre.createdAt >= :startOfToday AND " +
            "dre.createdAt < :startOfTmr")
    Optional<DailyRecordEntity> findTodayByUserId(@Param("userId") Long userId,
                                                  @Param("startOfToday") LocalDateTime startOfToday,
                                                  @Param("startOfTmr") LocalDateTime startOfTmr);
}
