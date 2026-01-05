package io.jaamong.recordMCI.infrastructure;

import io.jaamong.recordMCI.domain.entity.DailyRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyRecordJpaRepository extends JpaRepository<DailyRecordEntity, Long> {

    @Query("select dre from DailyRecordEntity dre " +
            "where dre.userEntity.id = :id AND " +
            "dre.date = :date")
    Optional<DailyRecordEntity> findDateByUserId(@Param("id") Long userId,
                                                 @Param("date") LocalDate date);

    @Query("select dre from DailyRecordEntity dre " +
            "where dre.userEntity.id = :id and " +
            "dre.date between :startOfMonth and :endOfMonth")
    List<DailyRecordEntity> findMonthByUserIdAndYearMonth(@Param("id") Long userId,
                                                          @Param("startOfMonth") LocalDate startOfMonth,
                                                          @Param("endOfMonth") LocalDate endOfMonth);
}
