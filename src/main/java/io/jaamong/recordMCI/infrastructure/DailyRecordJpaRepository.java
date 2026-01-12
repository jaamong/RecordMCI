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
            "dre.recordDate = :recordDate")
    Optional<DailyRecordEntity> findDateByUserId(@Param("id") Long userId,
                                                 @Param("recordDate") LocalDate date);

    @Query("select dre from DailyRecordEntity dre " +
            "left join fetch dre.foodEntityList " +  // fetch join으로 XToMany 연관되어 있는 엔티티는 1개만 가능. 그 이상은 -> MultipleBagFetchException.
//            "left join fetch dre.activityEntityList " +  // fetch join으로 최대한 성능 보장 후 batch_fetch_size 적용
            "where dre.userEntity.id = :id and " +
            "dre.recordDate between :startOfMonth and :endOfMonth")
    List<DailyRecordEntity> findMonthByUserIdAndYearMonth(@Param("id") Long userId,
                                                          @Param("startOfMonth") LocalDate startOfMonth,
                                                          @Param("endOfMonth") LocalDate endOfMonth);
}
