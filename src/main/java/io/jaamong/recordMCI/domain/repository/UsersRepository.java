package io.jaamong.recordMCI.domain.repository;

import io.jaamong.recordMCI.api.exception.CustomRuntimeException;
import io.jaamong.recordMCI.api.exception.ErrorCode;
import io.jaamong.recordMCI.domain.entity.UserEntity;
import io.jaamong.recordMCI.infrastructure.UsersJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UsersRepository {

    private final UsersJpaRepository usersJpaRepository;

    public UserEntity getById(Long userId) {
        return usersJpaRepository.findById(userId).orElseThrow(
                () -> new CustomRuntimeException(ErrorCode.NOT_FOUND_USER));
    }
}
