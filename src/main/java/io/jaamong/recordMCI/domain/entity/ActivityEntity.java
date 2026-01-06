package io.jaamong.recordMCI.domain.entity;

import io.jaamong.recordMCI.api.exception.CustomRuntimeException;
import io.jaamong.recordMCI.api.exception.ErrorCode;
import io.jaamong.recordMCI.domain.dto.Activity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityEntity extends BaseEntity {

    @Column(name = "activity_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_record_id")
    private DailyRecordEntity dailyRecordEntity;

    private String name;  // initial: "걷기"(Walk), "성경필사"(Bible transcribe), "컬러링북"(Coloring book)

    private boolean completed = false;  // For the checkbox

    // Optional fields - only populated when relevant
    private Integer totalSteps;          // null for non-Walk activities
    private Integer totalHours;          // null for non-Walk activities
    private Integer totalMinutes;        // null for non-Walk activities

    @Builder
    public ActivityEntity(DailyRecordEntity dailyRecordEntity, String name, boolean completed, Integer totalSteps, Integer totalHours, Integer totalMinutes) {
        this.dailyRecordEntity = dailyRecordEntity;
        this.name = name;
        this.completed = completed;
        this.totalSteps = totalSteps;
        this.totalHours = totalHours;
        this.totalMinutes = totalMinutes;
    }

    public static ActivityEntity of(DailyRecordEntity dailyRecordEntity, String name) {
        ActivityEntity activityEntity = builder()
                .dailyRecordEntity(dailyRecordEntity)
                .name(name)
                .build();

        if (name.equals("걷기")) {
            activityEntity.totalSteps = 0;
            activityEntity.totalHours = 0;
            activityEntity.totalMinutes = 0;
        }

        return activityEntity;
    }

    public Activity toModel() {
        return Activity.builder()
                .id(id)
                .dailyRecordId(dailyRecordEntity.getId())
                .name(name)
                .completed(completed)
                .totalSteps(totalSteps)
                .totalHours(totalHours)
                .totalMinutes(totalMinutes)
                .build();
    }

    public void mapDailyRecord(DailyRecordEntity dailyRecordEntity) {
        this.dailyRecordEntity = dailyRecordEntity;
    }

    public void invertCompleted() {
        this.completed = !this.completed;
    }

    public void updateWalk(int totalSteps, int totalHours, int totalMinutes) {
        if (this.completed) {
            this.totalSteps = totalSteps;
            this.totalHours = totalHours;
            this.totalMinutes = totalMinutes;
        } else {
            throw new CustomRuntimeException(ErrorCode.INVALID_ACTIVITY_WALK_UPDATE_REQUEST);
        }
    }

    @Override
    public String toString() {
        return "ActivityEntity{" +
                "id=" + id +
                ", dailyRecordEntity=" + dailyRecordEntity.getId() +
                ", name=" + name +
                ", completed=" + completed +
                ", totalSteps=" + totalSteps +
                ", totalHours=" + totalHours +
                ", totalMinutes=" + totalMinutes +
                '}';
    }
}
