package io.jaamong.recordMCI.infrastructure;

import io.jaamong.recordMCI.domain.entity.FoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodJpaRepository extends JpaRepository<FoodEntity, Long> {
}
