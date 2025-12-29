package io.jaamong.recordMCI.infrastructure;

import io.jaamong.recordMCI.domain.entity.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityJpaRepository extends JpaRepository<ActivityEntity, Long> {
}
