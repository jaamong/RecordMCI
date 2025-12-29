package io.jaamong.recordMCI.domain.dto;

import lombok.Builder;

public record Users(
        Long id,
        String username) {

    @Builder
    public Users {
    }
}
