package io.jaamong.recordMCI.infrastructure;

import io.jaamong.recordMCI.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersJpaRepository extends JpaRepository<UserEntity, Long> {
}
