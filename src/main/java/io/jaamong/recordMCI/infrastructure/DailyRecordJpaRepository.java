package io.jaamong.recordMCI.infrastructure;

import io.jaamong.recordMCI.domain.entity.DailyRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyRecordJpaRepository extends JpaRepository<DailyRecordEntity, Long> {

    @Query("select dre from DailyRecordEntity dre " +
            "where dre.userEntity.id = :userId AND " +
            "dre.date = :date")
    Optional<DailyRecordEntity> findDateByUserId(@Param("userId") Long userId,
                                                 @Param("date") LocalDate date);
}
