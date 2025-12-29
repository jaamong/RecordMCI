package io.jaamong.recordMCI.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityType {
    WALK("걷기"),
    BIBLE_TRANSCRIBE("성경필사"),
    COLORING_BOOK("색칠공부");

    private final String type;
}
